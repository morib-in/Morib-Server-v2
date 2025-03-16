package org.morib.server.global.sse.application.repository;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.morib.server.domain.relationship.application.FetchRelationshipService;
import org.morib.server.domain.relationship.infra.Relationship;
import org.morib.server.global.exception.SSEConnectionException;
import org.morib.server.global.message.ErrorMessage;
import org.morib.server.global.sse.application.event.SseDisconnectEvent;
import org.morib.server.global.sse.application.event.SseHeartbeatEvent;
import org.morib.server.global.sse.application.event.SseTimeoutEvent;
import org.morib.server.global.userauth.CustomUserDetails;
import org.morib.server.global.userauth.PrincipalHandler;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import static org.morib.server.global.common.Constants.SSE_EVENT_COMPLETION;
import static org.morib.server.global.common.Constants.SSE_TIMEOUT;

@Repository
@Slf4j
@RequiredArgsConstructor
public class SseRepository {

    public static final ConcurrentHashMap<Long, SseUserInfoWrapper> emitters = new ConcurrentHashMap<>();
    private final ApplicationEventPublisher eventPublisher;

    public SseEmitter create() {
        return new SseEmitter(SSE_TIMEOUT);
    }

    public SseEmitter add(Long userId, SseEmitter emitter, int elapsedTime, String runningCategoryName, Long taskId) {
        removeExistingEmitter(userId);

        emitters.put(userId, new SseUserInfoWrapper(emitter, elapsedTime, runningCategoryName, taskId));
        log.info("새 emitter 추가됨: {}", emitter);
        log.info("emitter 목록 크기: {}", emitters.size());
        
        if (emitter != null) {

            emitter.onCompletion(() -> {
                log.info("onCompletion 콜백 - userId: {}", userId);
                emitters.remove(userId);
                eventPublisher.publishEvent(new SseDisconnectEvent(this, userId));
            });

            emitter.onTimeout(() -> {
                log.info("onTimeout 콜백 - userId: {}", userId);
                emitter.complete();
                emitters.remove(userId);
                eventPublisher.publishEvent(new SseTimeoutEvent(this, userId));
            });

            emitter.onError(e -> {
                log.error("SseEmitter 오류 발생 - userId: {}, 오류: {}", userId, e.getMessage());
                emitter.complete();
                emitters.remove(userId);
                eventPublisher.publishEvent(new SseDisconnectEvent(this, userId));
            });
        }
        
        return emitter;
    }

    @Scheduled(fixedRate = 180000) // 5분마다 실행
    public void sendHeartbeat() {
        eventPublisher.publishEvent(new SseHeartbeatEvent(this));
    }

    @Scheduled(fixedRate = 600000) // 10분마다 실행
    public void cleanupStaleConnections() {
        log.info("오래된 SSE 연결 정리 시작...");
        long currentTime = System.currentTimeMillis();
        int removedCount = 0;
        
        for (Map.Entry<Long, SseUserInfoWrapper> entry : emitters.entrySet()) {
            SseEmitter emitter = entry.getValue().getSseEmitter();
            if (emitter != null) {
                // 연결 시간이 MAX_CONNECTION_TIME을 초과하면 제거
                if (currentTime - emitter.hashCode() > MAX_CONNECTION_TIME) {
                    try {
                        emitter.complete();
                    } catch (Exception e) {
                        log.warn("오래된 연결 완료 처리 중 오류 발생: {}", e.getMessage());
                    }
                    emitters.remove(entry.getKey());
                    removedCount++;
                    
                    // 연결 종료 이벤트 발행
                    eventPublisher.publishEvent(new SseDisconnectEvent(this, entry.getKey()));
                }
            } else {
                // emitter가 null인 경우 제거
                emitters.remove(entry.getKey());
                removedCount++;
            }
        }
        
        log.info("오래된 SSE 연결 정리 완료. 제거된 연결 수: {}, 남은 연결 수: {}", removedCount, emitters.size());
    }

    public SseEmitter getSseEmitterById(Long id) {
        return emitters.get(id) == null ? null : emitters.get(id).getSseEmitter();
    }

    public boolean isConnected(Long userId) {
        return emitters.containsKey(userId);
    }

    public List<SseEmitter> getAllSseEmitters() {
        return emitters.values().stream().map(SseUserInfoWrapper::getSseEmitter).toList();
    }

}
