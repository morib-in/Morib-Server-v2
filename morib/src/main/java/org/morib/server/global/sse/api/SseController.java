package org.morib.server.global.sse.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.morib.server.domain.timer.infra.TimerStatus;
import org.morib.server.global.common.ApiResponseUtil;
import org.morib.server.global.common.BaseResponse;
import org.morib.server.global.exception.InvalidQueryParameterException;
import org.morib.server.global.message.ErrorMessage;
import org.morib.server.global.message.SuccessMessage;
import org.morib.server.global.sse.application.service.SseService;
import org.morib.server.global.userauth.CustomUserDetails;
import org.morib.server.global.userauth.PrincipalHandler;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v2")
public class SseController {

    private final PrincipalHandler principalHandler;
    private final SseFacade sseFacade;
    private final SseService sseService;

    @GetMapping(value = "/sse/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> connect(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        Long userId = principalHandler.getUserIdFromUserDetails(customUserDetails);
        SseEmitter emitter = sseFacade.init(userId);
        return ResponseEntity.ok(emitter);
    }

    // 재연결 로직이 아닌 사용자 정보 업데이트용 api
    @GetMapping(value = "/sse/refresh")
    public ResponseEntity<BaseResponse<?>> refresh(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                @RequestHeader(required = false) String elapsedTime,
                                @RequestHeader(required = false) String taskId,
                                @RequestHeader(required = false) TimerStatus timerStatus){
        Long userId = principalHandler.getUserIdFromUserDetails(customUserDetails);
        int convertedElapsedTime = elapsedTime == null ? 0 : Integer.parseInt(elapsedTime);
        Long convertedTaskId = taskId == null ? null : Long.parseLong(taskId);
        sseFacade.refresh(userId, convertedElapsedTime, convertedTaskId, timerStatus);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS);
    }

}
