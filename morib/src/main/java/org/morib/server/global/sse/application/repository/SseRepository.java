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
        emitters.put(userId, new SseUserInfoWrapper(emitter, elapsedTime, runningCategoryName, taskId));
        log.info("new emitter added: {}", emitter);
        log.info("emitter list size: {}", emitters.size());
        log.info("emitter list: {}", emitters);

        if (emitter != null) {
            emitter.onCompletion(() -> {
                log.info("onCompletion callback");
                emitters.remove(userId);
                eventPublisher.publishEvent(new SseDisconnectEvent(this, userId));
            });

            emitter.onTimeout(() -> {
                log.info("onTimeout callback");
                emitter.complete();
                eventPublisher.publishEvent(new SseTimeoutEvent(this, userId));
            });
        }
        return emitter;
    }

    // 30초 간격으로 heartbeat 메시지 전송
    @Scheduled(fixedRate = 30000)
    public void sendHeartbeat() {
        eventPublisher.publishEvent(new SseHeartbeatEvent(this));
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
