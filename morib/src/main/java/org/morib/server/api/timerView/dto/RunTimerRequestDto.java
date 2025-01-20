package org.morib.server.api.timerView.dto;

public record RunTimerRequestDto(
        String runningCategoryName,
        Long taskId,
        int elapsedTime
) {
}
