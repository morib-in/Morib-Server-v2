package org.morib.server.api.timerView.dto;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

import jakarta.validation.constraints.NotNull;
import org.morib.server.annotation.IsToday;
import org.morib.server.domain.timer.infra.TimerSession;
import org.morib.server.domain.timer.infra.TimerStatus;

public record TimerDtos() {

    public record TimerRequest(@NotNull Long taskId, @NotNull @IsToday LocalDate targetDate) { }

    public record TimerStatusResponse(
            String runningCategoryName,
            Long selectedTaskId,
            int elapsedTime,
            TimerStatus timerStatus,
            LocalDate targetDate
    ) {
        public static TimerStatusResponse from(TimerSession timerSession) {
            return new TimerStatusResponse(
                    timerSession.getRunningCategoryName(),
                    timerSession.getSelectedTask() != null ? timerSession.getSelectedTask().getId() : null,
                    timerSession.getElapsedTime(),
                    timerSession.getTimerStatus(),
                    timerSession.getTargetDate()
            );
        }
    }
}