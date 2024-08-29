package org.morib.server.api.modalView.controller;

import lombok.RequiredArgsConstructor;
import org.morib.server.api.modalView.dto.CreateCategoryRequestDto;
import org.morib.server.api.modalView.facade.ModalViewFacade;
import org.morib.server.global.common.ApiResponseUtil;
import org.morib.server.global.common.BaseResponse;
import org.morib.server.global.message.SuccessMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2")
public class ModalViewController {

    private final ModalViewFacade modalViewFacade;

    @PostMapping("/categories")
    public ResponseEntity<BaseResponse<?>> create(// @AuthenticationPrincipal Long userId,
                                                @RequestBody CreateCategoryRequestDto createCategoryRequestDto) {
        Long userId = 1L;
        modalViewFacade.createCategory(userId, createCategoryRequestDto);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS);
    }

    @GetMapping("/mset/categories/{categoryId}")
    public ResponseEntity<BaseResponse<?>> fetchAllowedSiteByCategory(// @AuthenticationPrincipal Long userId,
        @PathVariable Long categoryId){
        Long mockUserId = 1L;
        return ApiResponseUtil.success(SuccessMessage.SUCCESS, modalViewFacade.
            fetchAllowedSiteByCategoryId(mockUserId, categoryId));
    }

    @GetMapping("/mset/tasks/{taskId}")
    public ResponseEntity<BaseResponse<?>> fetchAllowedSiteByTask(@PathVariable Long taskId){
        return ApiResponseUtil.success(SuccessMessage.SUCCESS, modalViewFacade.
            fetchAllowedSiteByTaskId(taskId));
    }




    // 카테고리 삭제
    @DeleteMapping("/categories/{categoryId}")
    public ResponseEntity<BaseResponse<?>> delete(@PathVariable("categoryId") Long categoryId) {
        modalViewFacade.deleteCategoryById(categoryId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS);
    }




}
