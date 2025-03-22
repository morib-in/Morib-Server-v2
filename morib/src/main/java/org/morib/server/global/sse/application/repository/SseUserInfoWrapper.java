package org.morib.server.global.sse.application.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.morib.server.domain.timer.infra.TimerStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SseUserInfoWrapper {
    private int elapsedTime;
    private String runningCategoryName;
    private Long taskId;
    private TimerStatus timerStatus;
    private LocalDateTime lastTimerStatusChangeTime;

    public static SseUserInfoWrapper of(int elapsedTime, String runningCategoryName, Long taskId, TimerStatus timerStatus, LocalDateTime lastTimerStatusChangeTime) {
        return new SseUserInfoWrapper(elapsedTime, runningCategoryName, taskId, timerStatus, lastTimerStatusChangeTime);
    }
}
