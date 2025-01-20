package org.morib.server.global.sse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Component
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
public class SseUserInfoWrapper {
    private SseEmitter sseEmitter;
    private int elapsedTime;
    private String runningCategoryName;
    private Long taskId;

    public static SseUserInfoWrapper create(SseEmitter sseEmitter, int elapsedTime, String runningCategoryName, Long taskId) {
        return new SseUserInfoWrapper(sseEmitter, elapsedTime, runningCategoryName, taskId);
    }
}
