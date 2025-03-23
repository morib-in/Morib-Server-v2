package org.morib.server.global.oauth2.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.morib.server.domain.user.infra.User;
import org.morib.server.domain.user.infra.UserRepository;
import org.morib.server.domain.user.infra.type.Platform;
import org.morib.server.global.exception.UnauthorizedException;
import org.morib.server.global.message.ErrorMessage;
import org.morib.server.global.oauth2.CustomOAuth2User;
import org.morib.server.global.oauth2.OAuthAttributes;
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

import java.util.Collections;
import java.util.Map;

import static org.morib.server.global.common.Constants.GOOGLE_REVOKE_URL;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;

    // TODO : 메서드 분리 리팩토링 필요
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("CustomOAuth2UserService.loadUser() 실행 - OAuth2 로그인 요청 진입");
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String principalName = (String) oAuth2User.getAttributes().get("sub");
        Platform platform = getSocialType(registrationId);
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        Map<String, Object> attributes = oAuth2User.getAttributes();
        OAuthAttributes extractAttributes = OAuthAttributes.of(platform, userNameAttributeName, attributes);
        User createdUser = getUser(extractAttributes, platform);
        return new CustomOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(createdUser.getRole().getKey())),
                attributes,
                extractAttributes.getNameAttributeKey(),
                createdUser.getRole(),
                createdUser.getId(),
                registrationId,
                principalName
        );
    }

    private Platform getSocialType(String registrationId) {
        return Platform.GOOGLE;
    }

    private User getUser(OAuthAttributes attributes, Platform platform) {
        User findUser = userRepository.findByPlatformAndSocialId(platform,
                attributes.getOauth2UserInfo().getId()).orElse(null);

        if(findUser == null) {
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
}
