package org.morib.server.api.timerView.dto;

import org.morib.server.domain.timer.infra.TimerStatus;

import java.time.LocalDate;

public record SaveTimerSessionRequestDto(
        Long taskId,
        int elapsedTime,
        TimerStatus timerStatus,
        LocalDate targetDate
) {
    public static SaveTimerSessionRequestDto of(Long taskId, int elapsedTime, TimerStatus timerStatus, LocalDate targetDate) {
        return new SaveTimerSessionRequestDto(taskId, elapsedTime, timerStatus, targetDate);
    }
}
