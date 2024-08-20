package org.morib.server.api.modalViewApi.controller;

import lombok.RequiredArgsConstructor;
import org.morib.server.api.modalViewApi.service.ModalViewFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2")
public class CategoryController {
    private final ModalViewFacade modalViewFacade;

    // 사용자의 모든 카테고리 조회
    @GetMapping("/categories")
    public ResponseEntity<?> fetch() {
        modalViewFacade.fetchCategories();
        return null;
    }


}
