package org.morib.server.api.settingView.controller;

import lombok.RequiredArgsConstructor;
import org.morib.server.api.settingView.dto.UpdateUserProfileRequestDto;
import org.morib.server.api.settingView.facade.SettingViewFacade;
import org.morib.server.global.common.BaseResponse;
import org.morib.server.global.common.util.ApiResponseUtil;
import org.morib.server.global.message.SuccessMessage;
import org.morib.server.global.userauth.CustomUserDetails;
import org.morib.server.global.userauth.PrincipalHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2")
public class SettingViewController {

    private final PrincipalHandler principalHandler;
    private final SettingViewFacade settingViewFacade;

    @GetMapping("/profiles")
    public ResponseEntity<BaseResponse<?>> fetch(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Long userId = principalHandler.getUserIdFromUserDetails(customUserDetails);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS, settingViewFacade.fetchUserProfile(userId));
    }

    @PutMapping("/profiles")
    public ResponseEntity<BaseResponse<?>> update(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                  @RequestBody UpdateUserProfileRequestDto updateUserProfileRequestDto) {
        Long userId = principalHandler.getUserIdFromUserDetails(customUserDetails);
        settingViewFacade.updateUserProfile(userId, updateUserProfileRequestDto);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS);
    }
}