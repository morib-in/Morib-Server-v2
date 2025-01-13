package org.morib.server.global.oauth2.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.morib.server.domain.user.UserManager;
import org.morib.server.domain.user.application.service.FetchUserService;
import org.morib.server.domain.user.infra.User;
import org.morib.server.domain.user.infra.UserRepository;
import org.morib.server.domain.user.infra.type.Role;
import org.morib.server.global.common.ApiResponseUtil;
import org.morib.server.global.common.SecretProperties;
import org.morib.server.global.common.TokenResponseDto;
import org.morib.server.global.exception.NotFoundException;
import org.morib.server.global.exception.UnauthorizedException;
import org.morib.server.global.jwt.JwtService;
import org.morib.server.global.message.ErrorMessage;
import org.morib.server.global.message.SuccessMessage;
import org.morib.server.global.oauth2.CustomOAuth2User;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static org.morib.server.global.common.Constants.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class  OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final FetchUserService fetchUserService;
    private final UserManager userManager;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    private final OAuth2AuthorizedClientService oAuth2AuthorizedClientService;
    private final SecretProperties secretProperties;

    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("OAuth2 Login 성공!");
        boolean isSignUp = false;
        try {
            CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
            if(oAuth2User.getRole() == Role.GUEST) { // 회원 가입
                User findUser = userRepository.findById(oAuth2User.getUserId())
                        .orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND));
                findUser.authorizeUser();
                isSignUp = true;
            }
            loginSuccess(response, oAuth2User, isSignUp);
        } catch (Exception e) {
            throw new UnauthorizedException(ErrorMessage.INVALID_TOKEN);
        }

    }

    private void loginSuccess(HttpServletResponse response, CustomOAuth2User oAuth2User, boolean isSignUp) throws IOException {
        String accessToken = jwtService.createAccessToken(oAuth2User.getUserId());
        String refreshToken = jwtService.createRefreshToken();
        jwtService.updateRefreshToken(oAuth2User.getUserId(), refreshToken);
        userManager.updateSocialRefreshToken(
                fetchUserService.fetchByUserId(
                        oAuth2User.getUserId()), getSocialRefreshTokenByAuthorizedClient(oAuth2User.getRegistrationId(), oAuth2User.getPrincipalName()));
        StringBuilder redirectUri = new StringBuilder(secretProperties.getClientRedirectUriProd());
        redirectUri.append(IS_SIGN_UP_QUERYSTRING).append(isSignUp);
        response.addCookie(getCookieForToken(ACCESS_TOKEN_SUBJECT, accessToken));
        response.addCookie(getCookieForToken(REFRESH_TOKEN_SUBJECT, refreshToken));
        response.sendRedirect(redirectUri.toString());
    }

    private String getSocialRefreshTokenByAuthorizedClient(String registrationId, String principalName) {
        OAuth2AuthorizedClient user = oAuth2AuthorizedClientService.loadAuthorizedClient(registrationId, principalName);
        OAuth2RefreshToken refreshToken = user.getRefreshToken();
        return refreshToken.getTokenValue();
    }

    private Cookie getCookieForToken(String sub, String token) {
        Cookie cookie = new Cookie(sub, token);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setDomain(secretProperties.getClientRedirectUriProd()); // 쿠키를 사용할 도메인
        cookie.setAttribute("SameSite", "None");
        return cookie;
    }
}
