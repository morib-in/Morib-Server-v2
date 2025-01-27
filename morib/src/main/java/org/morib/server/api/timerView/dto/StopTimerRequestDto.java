package org.morib.server.api.timerView.dto;

import java.time.LocalDate;

public record StopTimerRequestDto(
        LocalDate targetDate,
        int elapsedTime,
        String runningCategoryName,
        Long taskId
) {
}
