package org.morib.server.api.modalView.controller;

import lombok.RequiredArgsConstructor;
import org.morib.server.api.modalView.facade.ModalViewFacade;
import org.morib.server.global.common.ApiResponseUtil;
import org.morib.server.global.common.BaseResponse;
import org.morib.server.global.message.SuccessMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2")
public class CategoryController {
    private final ModalViewFacade modalViewFacade;

    @GetMapping("/categories")
    public ResponseEntity<BaseResponse<?>> fetchCategoriesByUser(//@AuthenticationPrincipal Long userId
    ) {
        Long userId = 1L;
        return ApiResponseUtil.success(SuccessMessage.SUCCESS, modalViewFacade.fetchCategories(userId));
    }
}
