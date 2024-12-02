package org.morib.server.api.homeView.controller;

import lombok.RequiredArgsConstructor;
import org.morib.server.api.homeView.dto.CreateTaskRequestDto;
import org.morib.server.api.homeView.facade.HomeViewFacade;
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
public class TaskController {
    private final HomeViewFacade homeViewFacade;
    private final PrincipalHandler principalHandler;

    @PostMapping("/tasks/{categoryId}")
    public ResponseEntity<BaseResponse<?>> createTask(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                      @PathVariable Long categoryId,
                                                      @RequestBody CreateTaskRequestDto createTaskRequestDto) {
        Long userId = principalHandler.getUserIdFromUserDetails(customUserDetails);
        homeViewFacade.createTask(userId, categoryId, createTaskRequestDto);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS);
    }

    @PostMapping("/tasks/{taskId}/status")
    public ResponseEntity<BaseResponse<?>> toggle(@PathVariable Long taskId) {
        homeViewFacade.toggleTaskStatus(taskId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS);
    }
}
