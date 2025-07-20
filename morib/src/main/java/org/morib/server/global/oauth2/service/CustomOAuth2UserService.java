package org.morib.server.global.oauth2.service;

import static org.morib.server.global.common.Constants.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.morib.server.domain.user.infra.User;
import org.morib.server.domain.user.infra.UserRepository;
import org.morib.server.domain.user.infra.type.Platform;
import org.morib.server.global.config.AppleProperties;
import org.morib.server.global.exception.UnauthorizedException;
import org.morib.server.global.message.ErrorMessage;
import org.morib.server.global.oauth2.CustomOAuth2User;
import org.morib.server.global.oauth2.OAuthAttributes;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.util.IOUtils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

	private final UserRepository userRepository;
	private final AppleProperties appleProperties;

	// TODO : 메서드 분리 리팩토링 필요
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		log.info("CustomOAuth2UserService.loadUser() 실행 - OAuth2 로그인 요청 진입");
		OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
		OAuth2User oAuth2User = delegate.loadUser(userRequest);
		String registrationId = userRequest.getClientRegistration().getRegistrationId();
		Platform platform = getSocialType(registrationId);
		String principalName = (String)oAuth2User.getAttributes().get("sub");
		String userNameAttributeName = userRequest.getClientRegistration()
			.getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
		OAuthAttributes oAuthAttributes;
		Map<String, Object> attributes;

		if (registrationId.equals("apple")) {
			log.info("[now in apple social login]");
			String idToken = userRequest.getAdditionalParameters().get("id_token").toString();
			attributes = decodeJwtTokenPayload(idToken);
			oAuthAttributes = OAuthAttributes.ofApple(platform, userNameAttributeName,
				attributes);

			log.info("now idToken {}, now attributes {}, now oAuthAttributes was this {}", idToken, attributes, oAuthAttributes);
		}else{
			 attributes = oAuth2User.getAttributes();
			 oAuthAttributes = OAuthAttributes.of(platform, userNameAttributeName, attributes);
		}

		User createdUser = getUser(oAuthAttributes, platform);

		log.info("now social login createdUser was this {}", createdUser);
		return new CustomOAuth2User(
			Collections.singleton(new SimpleGrantedAuthority(createdUser.getRole().getKey())),
			attributes,
			oAuthAttributes.getNameAttributeKey(),
			createdUser.getRole(),
			createdUser.getId(),
			registrationId,
			principalName
		);
	}

	private Map<String, Object> decodeJwtTokenPayload(String idToken) {
		Map<String, Object> jwtClaims = new HashMap<>();

		try {
			String[] parts = idToken.split("\\.");
			Base64.Decoder decoder = Base64.getUrlDecoder();

			byte[] decodeBytes = decoder.decode(parts[1].getBytes(StandardCharsets.UTF_8));
			String decodedString = new String(decodeBytes, StandardCharsets.UTF_8);
			ObjectMapper mapper = new ObjectMapper();

			Map<String, Object> map = mapper.readValue(decodedString, Map.class);
			jwtClaims.putAll(map);
		} catch (JsonProcessingException e) {
			log.error("Apple Decoded Error");
		}
		return jwtClaims;
	}

	private Platform getSocialType(String registrationId) {
		if(registrationId.equals("apple"))
			return Platform.APPLE;

		return Platform.GOOGLE;
	}

	private User getUser(OAuthAttributes attributes, Platform platform) {
		User findUser = userRepository.findByPlatformAndSocialId(platform,
			attributes.getOauth2UserInfo().getId()).orElse(null);

		if (findUser == null) {
			return saveUser(attributes, platform);
		}
		return findUser;
	}

	private User saveUser(OAuthAttributes attributes, Platform platform) {
		User createdUser = User.createByOAuth2UserInfo(platform, attributes.getOauth2UserInfo());
		return userRepository.saveAndFlush(createdUser);
	}

	public void withdrawInGoogle(String refreshToken) {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/x-www-form-urlencoded");
		String requestBody = "token=" + refreshToken;

		ResponseEntity<String> response = restTemplate.exchange(
			GOOGLE_REVOKE_URL,
			HttpMethod.POST,
			new HttpEntity<>(requestBody, headers),
			String.class
		);

		if (!response.getStatusCode().is2xxSuccessful()) {
			throw new UnauthorizedException(ErrorMessage.FAILED_WITHDRAW);
		}
	}

	public void withdrawInApple(String refreshToken) {
		try{
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/x-www-form-urlencoded");

		String url = UriComponentsBuilder.fromPath(APPLE_REVOKE_URL)
			.queryParam("token", refreshToken)
			.queryParam("client_id", appleProperties.getBundle_id())
			.queryParam("client_secret", createClientSecret())
			.queryParam("token_type_hint", "refresh_token")
			.build()
			.toString();

		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(null, headers),
			String.class);

		if (!response.getStatusCode().is2xxSuccessful()) {
			throw new UnauthorizedException(ErrorMessage.FAILED_WITHDRAW);
		}}catch (IOException e){
			log.error("apple_social logout failed {}", e.getMessage());
			throw new UnauthorizedException(ErrorMessage.FAILED_WITHDRAW);
		}
	}


	private String createClientSecret() throws IOException {

		Map<String, Object> jwtHeader = new HashMap<>();

		jwtHeader.put("kid", appleProperties.getKey_id());
		jwtHeader.put("alg", "ES256");

		return Jwts.builder()
			.setHeaderParams(jwtHeader)
			.setIssuer(appleProperties.getTeam_id())
			.setIssuedAt(new Date(System.currentTimeMillis()))
			.setExpiration(new Date(System.currentTimeMillis() + (1000 * 60 + 5)))
			.setAudience(APPLE_REVOKE_URL)
			.setSubject(appleProperties.getClient_id())
			.signWith(getPrivateKey(), SignatureAlgorithm.ES256)
			.compact();
	}

	private PrivateKey getPrivateKey() throws IOException {

		ClassPathResource resource = new ClassPathResource(appleProperties.getPath());

		InputStream in = resource.getInputStream();
		PEMParser pemParser = new PEMParser(
			new StringReader(IOUtils.readInputStreamToString(in, StandardCharsets.UTF_8)));

		PrivateKeyInfo object = (PrivateKeyInfo) pemParser.readObject();
		JcaPEMKeyConverter converter = new JcaPEMKeyConverter();

		return converter.getPrivateKey(object);
	}


}
