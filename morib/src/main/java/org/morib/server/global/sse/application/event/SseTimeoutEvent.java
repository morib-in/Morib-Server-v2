package org.morib.server.global.sse.application.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class SseTimeoutEvent extends ApplicationEvent {
    private final Long userId;

    public SseTimeoutEvent(Object source, Long userId) {
        super(source);
        this.userId = userId;
    }
}
