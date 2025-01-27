package org.morib.server.global.sse.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.morib.server.global.sse.application.service.SseService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

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
        SseEmitter emitter = sseFacade.init(userId);
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
