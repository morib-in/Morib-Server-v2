package org.morib.server.api.homeView.controller;

import lombok.RequiredArgsConstructor;
import org.morib.server.api.homeView.dto.fetch.HomeViewRequestDto;
import org.morib.server.api.homeView.facade.HomeViewFacade;
import org.morib.server.global.common.ApiResponseUtil;
import org.morib.server.global.common.BaseResponse;
import org.morib.server.global.message.SuccessMessage;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2")
public class HomeViewController {
    private final HomeViewFacade homeViewFacade;

    // 홈뷰 전체 조회
    @GetMapping("/home")
    public ResponseEntity<BaseResponse<?>> fetchHome( // @AuthenticationPrincipal Long userId,
                                                    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        // userId 임시 코드
        Long userId = 1L;
        return ApiResponseUtil.success(SuccessMessage.SUCCESS, homeViewFacade.fetchHome(HomeViewRequestDto.of(userId, startDate, endDate)));
    }

    // 오늘 나의 작업시간 조회
    @GetMapping("/today")
    public ResponseEntity<?> fetchTotalTimeOfToday() {
        homeViewFacade.fetchUserTimer();
        return null;
    }

    // 할일 추가 후 타이머 시작
    @PostMapping("/timers/start")
    public ResponseEntity<?> startTimer() {
        homeViewFacade.startTimer();
        return null;
    }


}
