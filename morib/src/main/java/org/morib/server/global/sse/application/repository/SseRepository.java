package org.morib.server.global.sse.application.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.morib.server.global.exception.SSEConnectionException;
import org.morib.server.global.message.ErrorMessage;
import org.morib.server.global.message.SseMessageBuilder;
import org.morib.server.global.sse.application.event.SseDisconnectEvent;
import org.morib.server.global.sse.application.event.SseHeartbeatEvent;
import org.morib.server.global.sse.application.event.SseTimeoutEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static org.morib.server.global.common.Constants.SSE_TIMEOUT;

@Repository
@Slf4j
@RequiredArgsConstructor
public class SseRepository {

    public static final ConcurrentHashMap<Long, SseEmitter> emitters = new ConcurrentHashMap<>();
    public static final ConcurrentHashMap<Long, SseUserInfoWrapper> userInfos = new ConcurrentHashMap<>();

    private final ApplicationEventPublisher eventPublisher;
    private final SseMessageBuilder sseMessageBuilder;

    public SseEmitter create() {
        return new SseEmitter(SSE_TIMEOUT);
    }

    public void removeInvalidEmitter(SseEmitter emitter) {
        if (emitter != null && emitters.containsValue(emitter)) {
            try {
                log.info("유효하지 않은 SseEmitter 제거: {}", emitter);
                emitter.completeWithError(new Exception("유효하지 않은 연결"));
                emitters.values().remove(emitter);
            } catch (Exception e) {
                log.warn("SseEmitter 완료 처리 중 오류 발생: {}", e.getMessage());
                emitters.values().remove(emitter);
            }
        }

    }

    public void removeExistingEmitter(Long userId) {
        if (emitters.containsKey(userId)) {
            SseEmitter oldEmitter = emitters.get(userId);
            if (oldEmitter != null) {
                try {
                    log.info("기존 SseEmitter 제거: {}", oldEmitter);
                    oldEmitter.complete();
                } catch (Exception e) {
                    oldEmitter.completeWithError(e);
                    log.warn("기존 SseEmitter 완료 처리 중 오류 발생: {}", e.getMessage());
                }
            }
//            emitters.remove(userId);
        }
    }

    public SseEmitter addSseEmitter(Long userId, SseEmitter emitter) {
        emitters.put(userId, emitter);
        log.info("새 userId : {} 의 emitter 추가됨: {}", userId, emitter);
        log.info("emitter 목록 크기: {}", emitters.size());
        
        if (emitter != null) {
            emitter.onCompletion(() -> {
                log.info("onCompletion 콜백 - userId: {}, emitter: {}", userId, emitter);
                eventPublisher.publishEvent(new SseDisconnectEvent(this, userId));
                emitters.remove(userId, emitter);
            });

            emitter.onTimeout(() -> {
                log.info("onTimeout 콜백 - userId: {}, emitter: {}", userId, emitter);
                emitter.complete();
            });

            emitter.onError(e -> {
                log.error("SseEmitter 오류 발생 - userId: {}, 오류: {}, emitter: {}", userId, e.getMessage(), emitter);
                eventPublisher.publishEvent(new SseDisconnectEvent(this, userId));
                emitter.completeWithError(e);
//                emitters.remove(userId, emitter); // 명시적으로 제거
            });
        }
        
        return emitter;
    }

    public SseUserInfoWrapper mergeUserInfoWrapper(Long userId, SseUserInfoWrapper userInfoWrapper) {
        userInfos.merge(userId, userInfoWrapper, (oldValue, newValue) -> {
            oldValue.setElapsedTime(oldValue.getElapsedTime() + newValue.getElapsedTime());
            oldValue.setRunningCategoryName(newValue.getRunningCategoryName());
            oldValue.setTaskId(newValue.getTaskId());
            oldValue.setTimerStatus(newValue.getTimerStatus());
            oldValue.setLastTimerStatusChangeTime(LocalDateTime.now());
            return oldValue;
        });
        return userInfos.get(userId);
    }


    @Scheduled(fixedRate = 30000)
    public void sendHeartbeat() {
        eventPublisher.publishEvent(new SseHeartbeatEvent(this));
    }

    @Scheduled(fixedRate = 600000) // 10분마다 실행
    public void cleanupStaleConnections() {
        log.info("== Null인 SSE 연결 정리 시작... ==");
        int removedCount = 0;
        
        for (Map.Entry<Long, SseEmitter> entry : emitters.entrySet()) {
            SseEmitter emitter = entry.getValue();
            if (emitter == null) {
                // emitter가 null인 경우 제거
                emitters.remove(entry.getKey());
                removedCount++;
            }
        }
        
        log.info("== NULL SSE 연결 정리 완료. 제거된 연결 수: {}, 남은 연결 수: {} == \n", removedCount, emitters.size());
    }

    public SseEmitter getSseEmitterById(Long id) {
        return emitters.get(id) == null ? null : emitters.get(id);
    }

    public SseUserInfoWrapper getSseUserInfoWrapperById(Long id) {
        return emitters.get(id) == null ? null : userInfos.get(id);
    }

    public boolean isConnected(Long userId) {
        return emitters.containsKey(userId);
    }

    public List<SseEmitter> getAllSseEmitters() {
        return emitters.values().stream()
                .filter(Objects::nonNull)
                .toList();
    }

    public int getConnectionCount() {
        return emitters.size();
    }

    public Set<Long> getConnectedUserIds() {
        return emitters.keySet();
    }


}
