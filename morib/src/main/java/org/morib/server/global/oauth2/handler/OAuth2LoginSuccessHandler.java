package org.morib.server.global.oauth2.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.morib.server.domain.user.infra.User;
import org.morib.server.domain.user.infra.UserRepository;
import org.morib.server.domain.user.infra.type.Role;
import org.morib.server.global.common.ApiResponseUtil;
import org.morib.server.global.common.TokenResponseDto;
import org.morib.server.global.exception.NotFoundException;
import org.morib.server.global.exception.UnauthorizedException;
import org.morib.server.global.jwt.JwtService;
import org.morib.server.global.message.ErrorMessage;
import org.morib.server.global.message.SuccessMessage;
import org.morib.server.global.oauth2.CustomOAuth2User;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static org.morib.server.global.common.Constants.REFRESH_TOKEN_SUBJECT;

@Slf4j
@Component
@RequiredArgsConstructor
public class  OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("OAuth2 Login 성공!");
        try {
            CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
            if(oAuth2User.getRole() == Role.GUEST) { // 회원 가입
                User findUser = userRepository.findById(oAuth2User.getUserId())
                        .orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND));
                findUser.authorizeUser();
            }
            loginSuccess(response, oAuth2User);
        } catch (Exception e) {
            throw new UnauthorizedException(ErrorMessage.INVALID_TOKEN);
        }
    }

    private void loginSuccess(HttpServletResponse response, CustomOAuth2User oAuth2User) throws IOException {
        String accessToken = jwtService.createAccessToken(oAuth2User.getUserId());
        String refreshToken = jwtService.createRefreshToken();
        jwtService.updateRefreshToken(oAuth2User.getUserId(), refreshToken);
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        response.addCookie(new Cookie(REFRESH_TOKEN_SUBJECT, refreshToken));
        response.getWriter().write(objectMapper.writeValueAsString(ApiResponseUtil.success(SuccessMessage.SUCCESS, TokenResponseDto.of(accessToken, oAuth2User.getUserId())).getBody()));
    }
}
