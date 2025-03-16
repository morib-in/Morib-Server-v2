package org.morib.server.global.sse.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @GetMapping(value = "/sse/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> connect(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        // 연결 전 메모리 사용량 측정
        long beforeMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        log.info("SSE 연결 전 메모리 사용량: {} bytes", beforeMemory);

        Long userId = principalHandler.getUserIdFromUserDetails(customUserDetails);
        SseEmitter emitter = sseFacade.init(userId);

        // 연결 후 메모리 사용량 측정
        long afterMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        log.info("SSE 연결 후 메모리 사용량: {} bytes", afterMemory);
        log.info("SSE 연결 당 메모리 사용량: {} bytes", afterMemory - beforeMemory);

        return ResponseEntity.ok(emitter);
    }

    @GetMapping("/sse/refresh")
    public ResponseEntity<SseEmitter> refresh(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                              @RequestHeader(required = false) String elapsedTime,
                                              @RequestHeader(required = false) String runningCategoryName,
                                              @RequestHeader(required = false) String taskId){
        Long userId = principalHandler.getUserIdFromUserDetails(customUserDetails);
        SseEmitter emitter = sseFacade.refresh(userId, UserInfoDtoForSseUserInfoWrapper.of(
                userId,
                elapsedTime == null ? 0 : Integer.parseInt(elapsedTime),
                runningCategoryName == null ? "" : runningCategoryName,
                taskId == null ? null : Long.parseLong(taskId)));
        return ResponseEntity.ok(emitter);
    }

}
