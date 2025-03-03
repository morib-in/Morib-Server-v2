package org.morib.server.global.sse.application.service;

import lombok.RequiredArgsConstructor;
import org.morib.server.global.exception.SSEConnectionException;
import org.morib.server.global.message.ErrorMessage;
import org.morib.server.global.message.SseMessageBuilder;
import org.morib.server.global.sse.application.event.SseDisconnectEvent;
import org.morib.server.global.sse.application.event.SseTimeoutEvent;
import org.morib.server.global.sse.application.repository.SseRepository;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;

import static org.morib.server.global.common.Constants.SSE_EVENT_COMPLETION;

@Component
@RequiredArgsConstructor
public class SseSender {

    public void sendEvent(SseEmitter emitter, String eventName, Object data) {
        try {
            if (emitter != null) {
                emitter.send(SseEmitter.event()
                        .name(eventName)
                        .data(data));
            }
        } catch (IOException e) {
            emitter.completeWithError(e);
            throw new SSEConnectionException(ErrorMessage.SSE_CONNECT_FAILED);
        }
    }

    public void broadcast(List<SseEmitter> emitters, String eventName, Object data) {
        for (SseEmitter targetEmitter : emitters) {
            try {
                if (targetEmitter != null) {
                    targetEmitter.send(SseEmitter.event()
                            .name(eventName)
                            .data(data));
                }
            } catch (IOException e) {
                targetEmitter.completeWithError(e);
                throw new SSEConnectionException(ErrorMessage.SSE_CONNECT_FAILED);
            }
        }
    }

    public void broadcastWithReconnectTime(List<SseEmitter> emitters, String eventName, Object data) {
        for (SseEmitter targetEmitter : emitters) {
            try {
                if (targetEmitter != null) {
                    SseEmitter.SseEventBuilder event = SseEmitter.event()
                            .data(data)
                            .name(eventName)
                            .reconnectTime(300000L);  // 300,000ms = 5분
                    targetEmitter.send(event);
                }
            } catch (IOException e) {
                targetEmitter.completeWithError(e);
                throw new SSEConnectionException(ErrorMessage.SSE_CONNECT_FAILED);

            }
        }
    }
}
