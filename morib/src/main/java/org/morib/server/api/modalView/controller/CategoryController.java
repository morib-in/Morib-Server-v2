package org.morib.server.api.modalView.controller;

import lombok.RequiredArgsConstructor;
import org.morib.server.api.modalView.dto.UpdateCategoryNameRequestDto;
import org.morib.server.api.modalView.facade.ModalViewFacade;
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
public class CategoryController {
    private final ModalViewFacade modalViewFacade;
    private final PrincipalHandler principalHandler;

    @GetMapping("/categories")
    public ResponseEntity<BaseResponse<?>> fetchCategoriesByUser(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Long userId = principalHandler.getUserIdFromUserDetails(customUserDetails);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS, modalViewFacade.fetchCategories(userId));
    }

    @PatchMapping("/categories/{categoryId}")
    public ResponseEntity<BaseResponse<?>> updateCategoryName(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                              @PathVariable("categoryId") Long categoryId,
                                                              @RequestBody UpdateCategoryNameRequestDto updateCategoryNameRequestDto) {
        Long userId = principalHandler.getUserIdFromUserDetails(customUserDetails);
        modalViewFacade.updateCategoryNameById(userId, categoryId, updateCategoryNameRequestDto);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS);
    }

}
