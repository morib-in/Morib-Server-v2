package org.morib.server.api.modalView.controller;

import lombok.RequiredArgsConstructor;
import org.morib.server.api.modalView.dto.CreateCategoryRequestDto;
import org.morib.server.api.modalView.facade.ModalViewFacade;
import org.morib.server.global.common.util.ApiResponseUtil;
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
public class ModalViewController {
    private final ModalViewFacade modalViewFacade;
    private final PrincipalHandler principalHandler;

    @PostMapping("/categories")
    public ResponseEntity<BaseResponse<?>> create(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                  @RequestBody CreateCategoryRequestDto createCategoryRequestDto) {
        Long userId = principalHandler.getUserIdFromUserDetails(customUserDetails);
        modalViewFacade.createCategory(userId, createCategoryRequestDto);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS);
    }

    @DeleteMapping("/categories/{categoryId}")
    public ResponseEntity<BaseResponse<?>> delete(@PathVariable("categoryId") Long categoryId) {
        modalViewFacade.deleteCategoryById(categoryId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS);
    }

    // deprecated
//    @GetMapping("/tabName")
//    public ResponseEntity<BaseResponse<?>> fetchTabNameByUrl(@RequestParam("url") String url) {
//        return ApiResponseUtil.success(SuccessMessage.SUCCESS, modalViewFacade.fetchTabNameByUrl(url));
//    }
}
