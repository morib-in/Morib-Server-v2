package org.morib.server.api.homeViewApi.controller;

import lombok.RequiredArgsConstructor;
import org.morib.server.api.homeViewApi.service.HomeViewFacade;
import org.morib.server.api.modalViewApi.service.ModalViewFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2")
public class TaskController {
    private final HomeViewFacade homeViewFacade;

    // 태스크 생성
    @PostMapping("/tasks")
    public ResponseEntity<?> create() {
        homeViewFacade.createTask();
        return null;
    }

    // 태스크 상태 변경 (체크박스)
    @PatchMapping("/tasks")
    public ResponseEntity<?> toggle() {
        homeViewFacade.toggleTaskStatus();
        return null;
    }
}
