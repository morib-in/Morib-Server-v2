package org.morib.server.api.loginView.controller;

import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.morib.server.api.loginView.dto.UserReissueResponseDto;
import org.morib.server.api.loginView.facade.UserAuthFacade;
import org.morib.server.domain.user.application.dto.ReissueTokenServiceDto;
import org.morib.server.global.common.ApiResponseUtil;
import org.morib.server.global.common.BaseResponse;
import org.morib.server.global.common.DataUtils;
import org.morib.server.global.exception.UnauthorizedException;
import org.morib.server.global.message.ErrorMessage;
import org.morib.server.global.message.SuccessMessage;
import org.morib.server.global.userauth.CustomUserDetails;
import org.morib.server.global.userauth.PrincipalHandler;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static org.morib.server.global.common.Constants.AUTHORIZATION;
import static org.morib.server.global.common.Constants.REFRESH_TOKEN_SUBJECT;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2")
public class UserAuthController {

    private final PrincipalHandler principalHandler;
    private final UserAuthFacade userAuthFacade;

    @PostMapping("/users/reissue")
    public ResponseEntity<BaseResponse<?>> reissue(@RequestHeader(AUTHORIZATION) final String accessToken,
                                                   @CookieValue(name = REFRESH_TOKEN_SUBJECT) final String refreshToken) {
        ReissueTokenServiceDto dto = userAuthFacade.reissue(accessToken, refreshToken);
        return ResponseEntity.status(SuccessMessage.SUCCESS.getHttpStatus())
                .header(HttpHeaders.SET_COOKIE, buildCookieForRefreshToken(dto.refreshToken()).toString())
                .body(BaseResponse.of(SuccessMessage.SUCCESS, UserReissueResponseDto.of(dto.accessToken())));
    }

    @DeleteMapping("/users/withdraw")
    public ResponseEntity<BaseResponse<?>> withdraw(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Long userId = principalHandler.getUserIdFromUserDetails(customUserDetails);
        userAuthFacade.withdraw(userId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS);
    }

    private ResponseCookie buildCookieForRefreshToken(String refreshToken) {
        return ResponseCookie
                .from(REFRESH_TOKEN_SUBJECT, refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(60)
                .domain("morib.in")
                .build();
    }
}
