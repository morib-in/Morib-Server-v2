package org.morib.server.global.oauth2.handler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.morib.server.domain.user.UserManager;
import org.morib.server.domain.user.application.service.FetchUserService;
import org.morib.server.domain.user.infra.User;
import org.morib.server.domain.user.infra.UserRepository;
import org.morib.server.domain.user.infra.type.Platform;
import org.morib.server.domain.user.infra.type.Role;
import org.morib.server.global.common.SecretProperties;
import org.morib.server.global.common.util.DataUtils;
import org.morib.server.global.config.DebugOAuth2AuthorizationRequestRepository;
import org.morib.server.global.exception.BusinessException;
import org.morib.server.global.exception.NotFoundException;
import org.morib.server.global.exception.UnauthorizedException;
import org.morib.server.global.jwt.JwtService;
import org.morib.server.global.message.ErrorMessage;
import org.morib.server.global.oauth2.CustomOAuth2User;
import org.morib.server.global.oauth2.userinfo.AppleOAuth2UserInfo;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Enumeration;
import java.util.Map;
import java.util.Objects;

import static org.morib.server.global.common.Constants.ACCESS_TOKEN_SUBJECT;
import static org.morib.server.global.common.Constants.REFRESH_TOKEN_SUBJECT;
import static org.morib.server.global.config.CustomAuthorizationRequestResolver.STATE_CLIENT_TYPE_KEY;
import static org.morib.server.global.config.CustomAuthorizationRequestResolver.STATE_CSRF_KEY;

@Slf4j
@Component
@RequiredArgsConstructor
public class  OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final OAuth2AuthorizedClientService oAuth2AuthorizedClientService;
    private final SecretProperties secretProperties;
    private final FetchUserService fetchUserService;
    private final UserManager userManager;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final String IS_ONBOARDING_COMPLETED = "isOnboardingCompleted";
    // Repository는 Spring Security가 자동으로 관리하므로 주입 불필요



    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        String encodedStateFromRequest = request.getParameter(OAuth2ParameterNames.STATE);
        String user = request.getParameter("user");
        ObjectMapper objectMapper1 = new ObjectMapper();

        Enumeration<String> parameterNames = request.getParameterNames();
        log.info("=== OAUTH2 CALLBACK RECEIVED ===");
        log.info("Request URI: {}", request.getRequestURI());
        log.info("success user parameter value was this : {}", user);
        log.info("Request Method: {}", request.getMethod());
        log.info("Session ID: {}", request.getSession(false) != null ? request.getSession(false).getId() : "NO SESSION");
        log.info("State from request (Apple 반환값): {}", encodedStateFromRequest);
        log.info("Code from Apple {}", parameterNames.nextElement());

        parameterNames.asIterator().forEachRemaining(clone-> log.info("now parameterName was this {}", clone));


        log.info("State length: {}", encodedStateFromRequest != null ? encodedStateFromRequest.length() : "null");
        log.info("All parameters: {}", request.getParameterMap());
        
        // Base64 디코딩 시도해서 JSON인지 확인
        if (encodedStateFromRequest != null) {
            try {
                byte[] decoded = Base64.getUrlDecoder().decode(encodedStateFromRequest);
                String decodedString = new String(decoded, StandardCharsets.UTF_8);
                log.info("Decoded state: {}", decodedString);
                if (decodedString.startsWith("{")) {
                    log.info("✅ Apple이 우리의 encodedState를 그대로 반환함");
                } else {
                    log.info("❌ Apple이 원본 state를 반환함 - 이게 문제!");
                }
            } catch (Exception e) {
                log.info("❌ State 디코딩 실패 - 원본 state일 가능성 높음: {}", e.getMessage());
            }
        }
        String clientType = "web"; // 기본값
        try {
            if (!StringUtils.hasText(encodedStateFromRequest)) {
                log.warn("State parameter is missing or invalid. Cannot determine client type. Defaulting to 'web'.");
            } else {
                log.info("now in else phase");
                
                // Apple은 원본 state 사용으로 JSON 파싱 불가 - 특별 처리
                CustomOAuth2User tempOAuth2User = (CustomOAuth2User) authentication.getPrincipal();
                String registrationId = tempOAuth2User.getRegistrationId();
                
                if ("apple".equals(registrationId)) {
                    log.info("Apple 로그인: 원본 state 사용, clientType을 web으로 기본 설정");
                    clientType = "web"; // Apple은 기본적으로 web으로 처리
                } else {
                    // 다른 provider는 기존 JSON 파싱 방식
                    byte[] decodedBytes = Base64.getUrlDecoder().decode(encodedStateFromRequest);
                    String stateJson = new String(decodedBytes, StandardCharsets.UTF_8);
                    Map<String, String> stateMap = objectMapper.readValue(stateJson, new TypeReference<Map<String, String>>() {});

                    log.info("now before clientType");
                    clientType = stateMap.getOrDefault(STATE_CLIENT_TYPE_KEY, "web");
                    String originalCsrfToken = stateMap.get(STATE_CSRF_KEY);
                    log.info("Successfully validated state. Client Type: {}, Original CSRF: {}", clientType, originalCsrfToken);
                }
            }

            CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
            log.info("now find oAuth2User {}", oAuth2User);
            
            User findUser = userRepository.findById(oAuth2User.getUserId())
                    .orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND));
            if (oAuth2User.getRole() == Role.GUEST) { // 회원 가입
                findUser.authorizeUser();
            }
            // 아니면 로그인으로 바로 직행
            loginSuccess(response, oAuth2User, findUser.isOnboardingCompleted(), clientType);
            
        } catch (Exception e) {
            log.error("OAuth2 authentication processing failed", e);
            throw new UnauthorizedException(ErrorMessage.INVALID_TOKEN);
        } finally {
            // 사용 완료 후 세션에서 제거
            //this.authorizationRequestRepository.removeAuthorizationRequest(request, response);
        }
    }

    private void loginSuccess(HttpServletResponse response, CustomOAuth2User oAuth2User, boolean isOnboardingCompleted, String clientType) throws IOException {
        log.info("login success 진입");
        String accessToken = jwtService.createAccessToken(oAuth2User.getUserId());
        String refreshToken = jwtService.createRefreshToken();
        jwtService.updateRefreshToken(oAuth2User.getUserId(), refreshToken);
        User user = fetchUserService.fetchByUserId(oAuth2User.getUserId());
        String socialRefreshToken = getSocialRefreshTokenByAuthorizedClient(oAuth2User);

        log.info("now socialRefreshToken {}", socialRefreshToken);
        //userManager.updateSocialRefreshToken(user, socialRefreshToken);
        userManager.updateSocialRefreshToken(user, socialRefreshToken);

        String targetRedirectUri;
        if ("electron".equalsIgnoreCase(clientType)) {
            targetRedirectUri = secretProperties.getClientRedirectUriElectron(); // Electron 용 URI 프로퍼티 필요
            log.info("Redirecting Electron client...");
        } else {
            targetRedirectUri = secretProperties.getClientRedirectUriProd(); // Web 용 URI
            log.info("Redirecting Web client...");
        }

        String redirectUri = UriComponentsBuilder.fromUriString(targetRedirectUri)
                .queryParam(IS_ONBOARDING_COMPLETED, isOnboardingCompleted)
                .queryParam(ACCESS_TOKEN_SUBJECT, accessToken)
                .queryParam(REFRESH_TOKEN_SUBJECT, refreshToken)
                .build()
                .toUri()
                .toString();
//        response.addCookie(dataUtils.getCookieForToken(REFRESH_TOKEN_SUBJECT, refreshToken));
        response.sendRedirect(redirectUri);
    }

    private String getSocialRefreshTokenByAuthorizedClient(CustomOAuth2User oAuth2User) {
        String registrationId = oAuth2User.getRegistrationId();
        String principalName = oAuth2User.getPrincipalName();
        log.info("getSocialRefreshTokenByAuthorizedClient 진입");
        log.info("registrationId: {}, principalName was this : {}", oAuth2User.getRegistrationId(), principalName);

        if(registrationId.equals("apple")) {
            Object socialRefreshToken = oAuth2User.getAttributes().get("refresh_token");
            log.info("social_refreshToken {}", socialRefreshToken);
            return socialRefreshToken != null ? socialRefreshToken.toString() : null;
        }


        OAuth2AuthorizedClient user = oAuth2AuthorizedClientService.loadAuthorizedClient(registrationId, principalName);
        if (user == null) {
            log.error("OAuth2AuthorizedClient is null! The client might not be registered.");
            return null;
        }
        OAuth2RefreshToken refreshToken = user.getRefreshToken();
        if (refreshToken == null) {
            log.error("Refresh token is null. The provider may not have issued one.");
        }
        return refreshToken.getTokenValue();
    }


}
