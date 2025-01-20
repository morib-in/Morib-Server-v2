package org.morib.server.global.sse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.morib.server.api.homeView.dto.fetch.FetchMyElapsedTimeResponseDto;
import org.morib.server.api.homeView.facade.HomeViewFacade;
import org.morib.server.domain.relationship.application.FetchRelationshipService;
import org.morib.server.domain.relationship.infra.Relationship;
import org.morib.server.domain.task.application.FetchTaskService;
import org.morib.server.domain.task.infra.Task;
import org.morib.server.domain.timer.TimerManager;
import org.morib.server.domain.timer.application.FetchTimerService;
import org.morib.server.domain.timer.infra.Timer;
import org.morib.server.global.exception.SSEConnectionException;
import org.morib.server.global.message.ErrorMessage;
import org.morib.server.global.userauth.PrincipalHandler;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static org.morib.server.global.common.Constants.SSE_TIMEOUT;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v2")
public class SseController {

    private final SseService sseService;
    private final SseFacade sseFacade;

    @GetMapping(value = "/sse/connect/{userId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> connect(
                                                // @AuthenticationPrincipal CustomUserDetails customUserDetails) {
                                                @PathVariable("userId") Long userId
                                                ){
//        Long userId = principalHandler.getUserIdFromUserDetails(customUserDetails);
        SseEmitter emitter = sseService.init(userId);
        return ResponseEntity.ok(emitter);
    }

    @PostMapping("/sse/refresh/{userId}")
    public ResponseEntity<SseEmitter> refresh(
                                                    // @AuthenticationPrincipal CustomUserDetails customUserDetails) {
                                                    @PathVariable("userId") Long userId,
                                                    @RequestBody UserInfoDtoForSseUserInfoWrapper userInfoDtoForSseUserInfoWrapper
                                                    ){
//        Long userId = principalHandler.getUserIdFromUserDetails(customUserDetails);
        SseEmitter emitter = sseFacade.refresh(userId, userInfoDtoForSseUserInfoWrapper);
        return ResponseEntity.ok(emitter);
    }

}
