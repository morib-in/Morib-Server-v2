package org.morib.server.global.oauth2.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.morib.server.api.loginView.facade.UserAuthFacade;
import org.morib.server.global.exception.UnauthorizedException;
import org.morib.server.global.jwt.JwtService;
import org.morib.server.global.message.ErrorMessage;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomLogoutHandler implements LogoutHandler {

    private final JwtService jwtService;
    private final UserAuthFacade userAuthFacade;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String accessToken = jwtService.extractAccessToken(request).orElseThrow(
                () -> new UnauthorizedException(ErrorMessage.INVALID_TOKEN)
        );
        Long userId = Long.valueOf(jwtService.extractId(accessToken).get());
        userAuthFacade.logout(userId);
    }
}
