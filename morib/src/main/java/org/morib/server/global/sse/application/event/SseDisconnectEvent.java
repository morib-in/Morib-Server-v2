package org.morib.server.global.sse.application.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class SseDisconnectEvent extends ApplicationEvent {

    private final Long userId;

    public SseDisconnectEvent(Object source, Long userId) {
        super(source);
        this.userId = userId;
    }

}
