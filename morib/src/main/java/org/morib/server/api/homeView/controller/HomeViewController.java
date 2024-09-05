package org.morib.server.api.homeView.controller;

import lombok.RequiredArgsConstructor;
import org.morib.server.api.homeView.dto.StartTimerRequestDto;
import org.morib.server.api.homeView.dto.fetch.HomeViewRequestDto;
import org.morib.server.api.homeView.facade.HomeViewFacade;
import org.morib.server.global.common.ApiResponseUtil;
import org.morib.server.global.common.BaseResponse;
import org.morib.server.global.message.SuccessMessage;
import org.morib.server.global.userauth.CustomUserDetails;
import org.morib.server.global.userauth.PrincipalHandler;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2")
public class HomeViewController {
    private final HomeViewFacade homeViewFacade;
    private final PrincipalHandler principalHandler;

    @GetMapping("/home")
    public ResponseEntity<BaseResponse<?>> fetchHome(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                     @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                     @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        Long userId = principalHandler.getUserIdFromUserDetails(customUserDetails);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
                homeViewFacade.fetchHome(HomeViewRequestDto.of(userId, startDate, endDate)));
    }

    @PostMapping("/timer/start")
    public ResponseEntity<BaseResponse<?>> startTimer(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate targetDate,
                                                      @RequestBody StartTimerRequestDto startTimerRequestDto) {
        Long userId = principalHandler.getUserIdFromUserDetails(customUserDetails);
        homeViewFacade.startTimer(userId, startTimerRequestDto, targetDate);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS);
    }

    @GetMapping("/timer")
    public ResponseEntity<BaseResponse<?>> fetchTotalElapsedTimeTodayByUser(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                                            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate targetDate) {
        Long userId = principalHandler.getUserIdFromUserDetails(customUserDetails);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
                homeViewFacade.fetchTotalElapsedTimeTodayByUser(userId, targetDate));
    }
}
