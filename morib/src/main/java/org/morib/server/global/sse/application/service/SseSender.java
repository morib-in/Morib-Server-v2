package org.morib.server.global.sse.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.morib.server.global.exception.SSEConnectionException;
import org.morib.server.global.message.ErrorMessage;
import org.morib.server.global.message.SseMessageBuilder;
import org.morib.server.global.sse.application.event.SseDisconnectEvent;
import org.morib.server.global.sse.application.event.SseTimeoutEvent;
import org.morib.server.global.sse.application.repository.SseRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.morib.server.global.common.Constants.MAX_FAILED_ATTEMPTS;
import static org.morib.server.global.common.Constants.SSE_EVENT_COMPLETION;

@Component
@RequiredArgsConstructor
@Slf4j
public class SseSender {

    // 실패한 전송 시도 횟수를 추적하기 위한 카운터
    private final AtomicInteger failedSendAttempts = new AtomicInteger(0);
    
    public void sendEvent(SseEmitter emitter, String eventName, Object data) {
        if (emitter == null) {
            log.warn("SseEmitter가 null입니다. 이벤트 전송을 건너뜁니다: {}", eventName);
            return;
        }
        
        try {
            emitter.send(SseEmitter.event()
                    .name(eventName)
                    .data(data));
            
            // 성공적인 전송 후 실패 카운터 재설정
            if (failedSendAttempts.get() > 0) {
                failedSendAttempts.set(0);
            }
        } catch (IOException e) {
            // 실패 카운터 증가
            int currentFailures = failedSendAttempts.incrementAndGet();
            
            // 최대 실패 횟수 초과 시 경고 로그
            if (currentFailures > MAX_FAILED_ATTEMPTS) {
                log.warn("SSE 이벤트 전송 실패 횟수가 임계값을 초과했습니다: {}", currentFailures);
            }
            
            log.error("SSE 이벤트 전송 실패: {}", e.getMessage());
            emitter.completeWithError(e);
        } catch (Exception e) {
            log.error("SSE 이벤트 전송 중 예상치 못한 오류 발생: {}", e.getMessage(), e);
            emitter.completeWithError(e);
        }
    }

    public void broadcast(List<SseEmitter> emitters, String eventName, Object data) {
        if (emitters == null || emitters.isEmpty()) {
            log.debug("브로드캐스트할 SseEmitter가 없습니다: {}", eventName);
            return;
        }
        
        log.debug("이벤트 브로드캐스트: {}, 대상 수: {}", eventName, emitters.size());
        
        for (SseEmitter targetEmitter : emitters) {
            try {
                if (targetEmitter != null) {
                    targetEmitter.send(SseEmitter.event()
                            .name(eventName)
                            .data(data));
                }
            } catch (IOException e) {
                log.error("SSE 브로드캐스트 실패: {}", e.getMessage());
                targetEmitter.completeWithError(e);
            } catch (Exception e) {
                log.error("SSE 브로드캐스트 중 예상치 못한 오류 발생: {}", e.getMessage(), e);
                targetEmitter.completeWithError(e);
            }
        }
    }
