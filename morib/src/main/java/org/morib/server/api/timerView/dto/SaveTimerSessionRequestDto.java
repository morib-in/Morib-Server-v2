package org.morib.server.api.timerView.dto;

import jakarta.validation.constraints.NotNull;
import org.morib.server.domain.timer.infra.TimerStatus;

import java.time.LocalDate;

public record SaveTimerSessionRequestDto(
        @NotNull
        Long taskId,
        @NotNull
        int elapsedTime,
        @NotNull
        TimerStatus timerStatus,
        @NotNull
        LocalDate targetDate
) {
    public static SaveTimerSessionRequestDto of(Long taskId, int elapsedTime, TimerStatus timerStatus, LocalDate targetDate) {
        return new SaveTimerSessionRequestDto(taskId, elapsedTime, timerStatus, targetDate);
    }
}
