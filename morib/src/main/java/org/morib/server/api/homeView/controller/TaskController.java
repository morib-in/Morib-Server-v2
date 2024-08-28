package org.morib.server.api.homeView.controller;

import lombok.RequiredArgsConstructor;
import org.morib.server.api.homeView.dto.CreateTaskRequestDto;
import org.morib.server.api.homeView.facade.HomeViewFacade;
import org.morib.server.global.common.ApiResponseUtil;
import org.morib.server.global.common.BaseResponse;
import org.morib.server.global.message.SuccessMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2")
public class TaskController {
    private final HomeViewFacade homeViewFacade;

    //태스크 생성
    @PostMapping("/tasks/{categoryId}")
    public ResponseEntity<BaseResponse<?>> createTask(//@AuthenticationPrincipal Long userId,
        @PathVariable Long categoryId, @RequestBody
    CreateTaskRequestDto createTaskRequestDto) {
        Long mockUserId = 1L;
        homeViewFacade.createTask(mockUserId, categoryId, createTaskRequestDto);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS);
    }

    // 태스크 상태 변경 (체크박스)
    @PatchMapping("/tasks/{taskId}/status")
    public ResponseEntity<BaseResponse<?>> toggle(@PathVariable Long taskId) {
        homeViewFacade.toggleTaskStatus(taskId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS);
    }
}
