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
import org.morib.server.global.common.DataUtils;
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
    private final UserRepository userRepository;
    private final OAuth2AuthorizedClientService oAuth2AuthorizedClientService;
    private final SecretProperties secretProperties;
    private final DataUtils dataUtils;
    private final FetchUserService fetchUserService;
    private final UserManager userManager;

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
        log.info("login success 진입");
        String accessToken = jwtService.createAccessToken(oAuth2User.getUserId());
        String refreshToken = jwtService.createRefreshToken();
        jwtService.updateRefreshToken(oAuth2User.getUserId(), refreshToken);
        log.info("oAuthUser.getUserId() : " + oAuth2User.getUserId());
        log.info("refreshToken : " + refreshToken);
        log.info("1 : User 파싱");
//         prod
        User user = fetchUserService.fetchByUserId(oAuth2User.getUserId());
        log.info("2 : Social Refresh Token Build");
        String socialRefreshToken = getSocialRefreshTokenByAuthorizedClient(oAuth2User.getRegistrationId(), oAuth2User.getPrincipalName());
        log.info("3 : SocialRefreshToken Update");
        userManager.updateSocialRefreshToken(user, socialRefreshToken);
        StringBuilder redirectUri = new StringBuilder(secretProperties.getClientRedirectUriProd());

        // dev
//        StringBuilder redirectUri = new StringBuilder(secretProperties.getClientRedirectUriDev());

        // common
        redirectUri.append(IS_SIGN_UP_QUERYSTRING).append(isSignUp);
        redirectUri.append("&accessToken=").append(accessToken);
        log.info("redirectUri : " + redirectUri.toString());
        response.addCookie(dataUtils.getCookieForToken(REFRESH_TOKEN_SUBJECT, refreshToken));
        log.info("Response Cookies: " + response.getHeaders("Set-Cookie"));
        response.sendRedirect(redirectUri.toString());
    }

    private String getSocialRefreshTokenByAuthorizedClient(String registrationId, String principalName) {
        log.info("getSocialRefreshTokenByAuthorizedClient 진입");
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
