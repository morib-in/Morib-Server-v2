package org.morib.server.api.timerView.dto;

import org.morib.server.domain.timer.infra.TimerSession;
import org.morib.server.domain.timer.infra.TimerStatus;

import java.time.LocalDate;

public record TimerResponseDto(
        String runningCategoryName,
        String taskName,
        Long selectedTaskId,
        int elapsedTime,
        TimerStatus timerStatus,
        LocalDate targetDate
) {
    public static TimerResponseDto from(TimerSession timerSession) {
        return new TimerResponseDto(
                timerSession.getRunningCategoryName(),
                timerSession.getSelectedTask() != null ? timerSession.getSelectedTask().getName() : null,
                timerSession.getSelectedTask() != null ? timerSession.getSelectedTask().getId() : null,
                timerSession.getElapsedTime(),
                timerSession.getTimerStatus(),
                timerSession.getTargetDate()
        );
    }
} 