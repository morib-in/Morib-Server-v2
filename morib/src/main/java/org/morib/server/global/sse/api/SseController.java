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
    public ResponseEntity<SseEmitter> connect(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        Long userId = principalHandler.getUserIdFromUserDetails(customUserDetails);
        SseEmitter emitter = sseFacade.init(userId);
        return ResponseEntity.ok(emitter);
    }

    @PostMapping("/sse/refresh")
    public ResponseEntity<SseEmitter> refresh(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                              @RequestBody UserInfoDtoForSseUserInfoWrapper userInfoDtoForSseUserInfoWrapper){
        Long userId = principalHandler.getUserIdFromUserDetails(customUserDetails);
        SseEmitter emitter = sseFacade.refresh(userId, userInfoDtoForSseUserInfoWrapper);
        return ResponseEntity.ok(emitter);
    }

}
