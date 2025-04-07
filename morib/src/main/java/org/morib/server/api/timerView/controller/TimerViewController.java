package org.morib.server.api.timerView.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.morib.server.api.timerView.dto.AssignAllowedGroupsRequestDto;
import org.morib.server.api.timerView.dto.SaveTimerSessionRequestDto;
import org.morib.server.api.timerView.facade.TimerViewFacade;
import org.morib.server.domain.timer.infra.TimerStatus;
import org.morib.server.global.common.util.ApiResponseUtil;
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
@RequestMapping("/api/v2")
@RequiredArgsConstructor
public class TimerViewController {

    private final TimerViewFacade timerViewFacade;
    private final PrincipalHandler principalHandler;

    @PostMapping("/timer/sync")
    public ResponseEntity<BaseResponse<?>> saveTimerSession(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                            @Valid @RequestBody SaveTimerSessionRequestDto saveTimerSessionRequestDto) {
        Long userId = principalHandler.getUserIdFromUserDetails(customUserDetails);
        timerViewFacade.saveTimerSession(userId, saveTimerSessionRequestDto);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS);
    }

    @GetMapping("/timer/ping")
    public ResponseEntity<BaseResponse<?>> timerPing(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                            @RequestParam Long taskId,
                                                            @RequestParam int elapsedTime,
                                                            @RequestParam TimerStatus timerStatus,
                                                            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate targetDate) {
        Long userId = principalHandler.getUserIdFromUserDetails(customUserDetails);
        SaveTimerSessionRequestDto saveTimerSessionRequestDto = SaveTimerSessionRequestDto.of(taskId, elapsedTime, timerStatus, targetDate);
        timerViewFacade.saveTimerSession(userId, saveTimerSessionRequestDto);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS);
    }

    @GetMapping("/timer/todo-card")
    public ResponseEntity<BaseResponse<?>> getTodoCards(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate targetDate) {
        Long userId = principalHandler.getUserIdFromUserDetails(customUserDetails);
        if(!targetDate.equals(LocalDate.now()))
            throw new IllegalArgumentException("오늘 당일의 날짜만 확인할 수 있습니다!");
        return ApiResponseUtil.success(SuccessMessage.SUCCESS, timerViewFacade.fetchTodoCard(userId, targetDate));
    }

    @GetMapping("/timer/friends")
    public ResponseEntity<BaseResponse<?>> getFriendsInfo(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Long userId = principalHandler.getUserIdFromUserDetails(customUserDetails);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS, timerViewFacade.fetchFriendsInfo(userId));
    }

    // 허용 서비스 조회
    @GetMapping("/timer/allowedGroups")
    public ResponseEntity<BaseResponse<?>> getAllowedGroupsAndAllowedSites(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Long userId = principalHandler.getUserIdFromUserDetails(customUserDetails);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS, timerViewFacade.fetchAllowedGroupsInTimer(userId));
    }

    // 허용 서비스 등록
    @PostMapping("/timer/allowedGroups")
    public ResponseEntity<BaseResponse<?>> assignAllowedGroupsInTimer(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                                      @RequestBody AssignAllowedGroupsRequestDto assignAllowedGroupsRequestDto) {
        Long userId = principalHandler.getUserIdFromUserDetails(customUserDetails);
        timerViewFacade.assignAllowedGroupsInTimer(userId, assignAllowedGroupsRequestDto);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS);
    }

}
