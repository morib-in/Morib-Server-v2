package org.morib.server.global.sse.application.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class SseHeartbeatEvent extends ApplicationEvent {

    public SseHeartbeatEvent(Object source) {
        super(source);
    }
}
