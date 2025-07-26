package org.morib.server.global.oauth2.converter;

import org.morib.server.domain.user.infra.UserRepository;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CompositeOAuth2AccessTokenResponseClient implements OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest>{

	private final AppleOAuth2AccessTokenResponseClient appleClient;
	private final DefaultAuthorizationCodeTokenResponseClient defaultClient;

	public CompositeOAuth2AccessTokenResponseClient(
		CustomRequestEntityConverter converter,
		UserRepository userRepository
		) {
		this.appleClient = new AppleOAuth2AccessTokenResponseClient(converter,
			userRepository);
		this.defaultClient = new DefaultAuthorizationCodeTokenResponseClient();
		defaultClient.setRequestEntityConverter(converter);
	}

	@Override
	public OAuth2AccessTokenResponse getTokenResponse(OAuth2AuthorizationCodeGrantRequest authorizationGrantRequest) {
		String registrationId = authorizationGrantRequest.getClientRegistration().getRegistrationId();

		log.info("🔍 Processing token exchange for registrationId: {}", registrationId);

		if ("apple".equals(registrationId)) {
			log.info("🍎 Using Apple custom token client");
			return appleClient.getTokenResponse(authorizationGrantRequest);
		} else {
			log.info("🌐 Using default token client for: {}", registrationId);
			return defaultClient.getTokenResponse(authorizationGrantRequest);
		}	}
}
