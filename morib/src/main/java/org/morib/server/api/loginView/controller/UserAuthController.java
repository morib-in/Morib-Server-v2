package org.morib.server.api.loginView.controller;

import lombok.RequiredArgsConstructor;
import org.morib.server.api.loginView.facade.UserAuthFacade;
import org.morib.server.global.common.ApiResponseUtil;
import org.morib.server.global.common.BaseResponse;
import org.morib.server.global.exception.UnauthorizedException;
import org.morib.server.global.message.ErrorMessage;
import org.morib.server.global.message.SuccessMessage;
import org.morib.server.global.userauth.CustomUserDetails;
import org.morib.server.global.userauth.PrincipalHandler;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import static org.morib.server.global.common.Constants.AUTHORIZATION;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2")
public class UserAuthController {

    private final PrincipalHandler principalHandler;
    private final UserAuthFacade userAuthFacade;

    @PostMapping("/users/reissue")
    public ResponseEntity<BaseResponse<?>> reissue(@RequestHeader(AUTHORIZATION) final String refreshToken) {
        return ApiResponseUtil.success(SuccessMessage.SUCCESS, userAuthFacade.reissue(refreshToken));
    }

    @DeleteMapping("/users/withdraw")
    public ResponseEntity<BaseResponse<?>> withdraw(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Long userId = principalHandler.getUserIdFromUserDetails(customUserDetails);
        userAuthFacade.withdraw(userId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS);
    }
}
