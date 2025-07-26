package org.morib.server.global.oauth2.converter;

import java.util.Map;

import org.morib.server.domain.user.infra.UserRepository;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class AppleOAuth2AccessTokenResponseClient implements OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> {


	private final CustomRequestEntityConverter customRequestEntityConverter;
	private final ObjectMapper objectMapper = new ObjectMapper();


	@Override
	public OAuth2AccessTokenResponse getTokenResponse(OAuth2AuthorizationCodeGrantRequest authorizationGrantRequest) {

		RequestEntity<?> request = customRequestEntityConverter.convert(authorizationGrantRequest);

		MultiValueMap<String, String> body = (MultiValueMap<String, String>)request.getBody();
		RestClient restClient = RestClient.create();

		try{
			String responseBody = restClient.post()
				.uri("https://appleid.apple.com/auth/token")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.body(body)
				.retrieve()
				.onStatus(HttpStatusCode::isError, (request2, response) -> {
					log.error("Apple token exchange failed with status: {}", response.getStatusCode());
					throw new OAuth2AuthenticationException(
						new OAuth2Error("token_exchange_failed"),
						"Token exchange failed with status: " + response.getStatusCode());
				})
				.body(String.class);

			// Apple 응답 파싱
			Map<String, Object> tokenResponse = objectMapper.readValue(responseBody, Map.class);
			String accessToken = (String) tokenResponse.get("access_token");
			String refreshToken = (String) tokenResponse.get("refresh_token");
			String idToken = (String) tokenResponse.get("id_token");
			Integer expiresIn = (Integer) tokenResponse.get("expires_in");

			log.info("🎯 Apple token response - access: {}, refresh: {}, id: {}",
				accessToken != null ? "✓" : "✗",
				refreshToken != null ? "✓" : "✗",
				idToken != null ? "✓" : "✗");

			// refresh_token을 ThreadLocal에 저장
			if (refreshToken != null) {
				AppleTokenHolder.setRefreshToken(refreshToken);
				log.info("🎯 Apple refresh_token successfully captured!");
			}

			if(tokenResponse != null) {
				log.info("tokenResponse: {}", objectMapper.writeValueAsString(tokenResponse));
			}

			return OAuth2AccessTokenResponse.withToken(accessToken)
				.tokenType(OAuth2AccessToken.TokenType.BEARER)
				.expiresIn(expiresIn != null ? expiresIn : 3600)
				.refreshToken(refreshToken)
				.additionalParameters(Map.of("id_token", idToken, "refresh_token", refreshToken))
				.build();
		}catch (JsonProcessingException e){
			log.error("Failed to parse Apple token response", e);
			throw new OAuth2AuthenticationException(
				new OAuth2Error("token_parse_failed"), "Token parsing failed");
		}

	}
}
