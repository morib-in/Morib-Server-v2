package org.morib.server.api.modalView.controller;

import lombok.RequiredArgsConstructor;
import org.morib.server.api.modalView.facade.ModalViewFacade;
import org.morib.server.global.common.ApiResponseUtil;
import org.morib.server.global.common.BaseResponse;
import org.morib.server.global.message.SuccessMessage;
import org.morib.server.global.userauth.CustomUserDetails;
import org.morib.server.global.userauth.PrincipalHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2")
public class RelationshipController {

    private final PrincipalHandler principalHandler;
    private final ModalViewFacade modalViewFacade;

    @GetMapping("/friends")
    public ResponseEntity<BaseResponse<?>> fetchFriends(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Long userId = principalHandler.getUserIdFromUserDetails(customUserDetails);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS, modalViewFacade.fetchFriends(userId));
    }
}
