package org.morib.server.api.modalView.controller;

import lombok.RequiredArgsConstructor;
import org.morib.server.api.modalView.facade.ModalViewFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2")
public class ModalViewController {
    private final ModalViewFacade modalViewFacade;

    // 카테고리 생성
    @PostMapping("/categories")
    public ResponseEntity<?> create() {
        return null;
    }

    // 카테고리 삭제
    @DeleteMapping("/categories")
    public ResponseEntity<?> delete() {
        return null;
    }
}
