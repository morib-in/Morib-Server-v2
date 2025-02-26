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
import org.morib.server.global.sse.application.event.SseTimeoutEvent;
import org.springframework.context.ApplicationEventPublisher;
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

        return emitter;
    }

    public void remove(SseEmitter emitter) {
        emitter.complete();
    }

    public SseEmitter getSseEmitterById(Long id) {
        return emitters.get(id) == null ? null : emitters.get(id).getSseEmitter();
    }

    public void pushNotifications(String fromName, Long toId, String msg) {
        SseUserInfoWrapper sseUserInfoWrapper = this.emitters.get(toId);
        SseEmitter emitter = sseUserInfoWrapper.getSseEmitter();
        if (emitter != null) {
            try {
                emitter.send(
                        SseEmitter.event().name("friendRequest").data(fromName + msg));
            }catch (IOException e) {
                emitter.completeWithError(e);
                emitters.remove(toId); 
            }
        }
    }

    public boolean isConnected(Long userId) {
        return emitters.containsKey(userId);
    }

    public void broadcast(Long userId, Object data, String eventName, List<Relationship> relationships) {
        relationships.forEach(relationship -> {
            Long targetUserId = relationship.getUser().getId().equals(userId)
                    ? relationship.getFriend().getId()
                    : relationship.getUser().getId();

            if (isConnected(targetUserId)) {
                try {
                    SseEmitter findEmitter = emitters.get(targetUserId).getSseEmitter();
                    findEmitter.send(SseEmitter.event()
                            .name(eventName)
                            .data(data));
                } catch (IOException e) {
                    throw new SSEConnectionException(ErrorMessage.SSE_CONNECT_FAILED);
                }
            }
        });
    }


}
