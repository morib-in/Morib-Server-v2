package org.morib.server.global.sse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.morib.server.global.common.ApiResponseUtil;
import org.morib.server.global.common.BaseResponse;
import org.morib.server.global.exception.SSEConnectionException;
import org.morib.server.global.message.ErrorMessage;
import org.morib.server.global.message.SuccessMessage;
import org.morib.server.global.userauth.CustomUserDetails;
import org.morib.server.global.userauth.PrincipalHandler;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

import static org.morib.server.global.common.Constants.SSE_TIMEOUT;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v2")
public class SseController {

    private final SseEmitters sseEmitters;
    private final PrincipalHandler principalHandler;

    @GetMapping(value = "/sse/connect/{userId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> connect(
                                                // @AuthenticationPrincipal CustomUserDetails customUserDetails) {
                                                @PathVariable("userId") Long userId
                                                ){
//        Long userId = principalHandler.getUserIdFromUserDetails(customUserDetails);
        SseEmitter emitter = new SseEmitter(SSE_TIMEOUT);
        sseEmitters.add(userId, emitter);
        try {
            emitter.send(SseEmitter.event()
                    .name("connect")
                    .data("connected!"));
        } catch (IOException e) {
            throw new SSEConnectionException(ErrorMessage.SSE_CONNECT_FAILED);
        }
        return ResponseEntity.ok(emitter);
    }

    @GetMapping("/sse/refresh")
    public ResponseEntity<SseEmitter> refresh(
                                                    // @AuthenticationPrincipal CustomUserDetails customUserDetails) {
                                                    @PathVariable("userId") Long userId
                                                    ){
        SseEmitter emitter = new SseEmitter(SSE_TIMEOUT);
        sseEmitters.add(userId, emitter);
        return ResponseEntity.ok(emitter);
    }

}
