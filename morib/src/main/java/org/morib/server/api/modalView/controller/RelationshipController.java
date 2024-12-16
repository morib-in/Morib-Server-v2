package org.morib.server.api.modalView.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.morib.server.api.modalView.dto.CreateRelationshipRequestDto;
import org.morib.server.api.modalView.facade.ModalViewFacade;
import org.morib.server.global.common.ApiResponseUtil;
import org.morib.server.global.common.BaseResponse;
import org.morib.server.global.message.SuccessMessage;
import org.morib.server.global.userauth.CustomUserDetails;
import org.morib.server.global.userauth.PrincipalHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2")
public class RelationshipController {

    private final PrincipalHandler principalHandler;
    private final ModalViewFacade modalViewFacade;

    @GetMapping("/friends")
    public ResponseEntity<BaseResponse<?>> fetchRelationships(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Long userId = principalHandler.getUserIdFromUserDetails(customUserDetails);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS, modalViewFacade.fetchConnectedRelationships(userId));
    }

    @GetMapping("/friends/requests")
    public ResponseEntity<BaseResponse<?>> fetchRelationshipRequests(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Long userId = principalHandler.getUserIdFromUserDetails(customUserDetails);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS, modalViewFacade.fetchUnconnectedRelationships(userId));
    }

    @PostMapping("/friends/requests")
    public ResponseEntity<BaseResponse<?>> createRelationship(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                              @RequestBody @Valid CreateRelationshipRequestDto createRelationshipRequestDto) {
        Long userId = principalHandler.getUserIdFromUserDetails(customUserDetails);
        modalViewFacade.createRelationship(userId, createRelationshipRequestDto);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS);
    }

    @PostMapping("/friends/requests/{friendId}/accept")
    public ResponseEntity<BaseResponse<?>> acceptPendingFriendRequest(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                                      @PathVariable("friendId") Long friendId) {
        Long userId = principalHandler.getUserIdFromUserDetails(customUserDetails);
        modalViewFacade.acceptPendingFriendRequest(userId, friendId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS);
    }

    @DeleteMapping("/friends/requests/{friendId}")
    public ResponseEntity<BaseResponse<?>> cancelPendingFriendRequest(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                                      @PathVariable("friendId") Long friendId) {
        Long userId = principalHandler.getUserIdFromUserDetails(customUserDetails);
        modalViewFacade.cancelPendingFriendRequest(userId, friendId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS);
    }

    @DeleteMapping("/friends/requests/{friendId}/reject")
    public ResponseEntity<BaseResponse<?>> rejectPendingFriendRequest(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                                      @PathVariable("friendId") Long friendId) {
        Long userId = principalHandler.getUserIdFromUserDetails(customUserDetails);
        modalViewFacade.rejectPendingFriendRequest(userId, friendId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS);
    }

}
