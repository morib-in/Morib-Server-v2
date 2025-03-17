package org.morib.server.global.sse.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.morib.server.domain.relationship.application.FetchRelationshipService;
import org.morib.server.global.message.SseMessageBuilder;
import org.morib.server.global.sse.application.event.SseDisconnectEvent;
import org.morib.server.global.sse.application.event.SseHeartbeatEvent;
import org.morib.server.global.sse.application.event.SseTimeoutEvent;
import org.morib.server.global.sse.application.service.SseSender;
import org.morib.server.global.sse.application.service.SseService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

import static org.morib.server.global.common.Constants.*;

@RequiredArgsConstructor
@Component
@Slf4j
public class SseEventHandler {

    private final SseService sseService;
    private final SseSender sseSender;
    private final SseMessageBuilder sseMessageBuilder;
    private final FetchRelationshipService fetchRelationshipService;

    @EventListener
    public void handleSseCompletion(SseDisconnectEvent event) {
        log.info("SseDisconnectEvent received for userId: {}", event.getUserId());
        List<Long> targetUserIds = fetchRelationshipService.fetchConnectedRelationshipAndClassify(event.getUserId());
        List<SseEmitter> targetEmitters = sseService.fetchConnectedSseEmittersById(targetUserIds);
        sseSender.broadcast(targetEmitters, SSE_EVENT_COMPLETION, sseMessageBuilder.buildDisconnectionMessage(event.getUserId()));
    }

    @EventListener
    public void handleSseTimeout(SseTimeoutEvent event) {
        log.info("SseTimeoutEvent received for userId: {}", event.getUserId());
        sseSender.sendEvent(sseService.fetchSseEmitterByUserId(event.getUserId()), SSE_EVENT_TIME_OUT, sseMessageBuilder.buildTimeoutMessage(event.getUserId()));
    }

    @EventListener
    public void handleSseHeartbeat(SseHeartbeatEvent event) {
        log.info("SseHeartbeat received");
        List<SseEmitter> targetEmitters = sseService.fetchAllConnectedSseEmitters();
        sseSender.sendHeartbeat(targetEmitters);
    }
}
