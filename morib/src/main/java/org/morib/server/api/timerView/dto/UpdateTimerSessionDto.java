package org.morib.server.api.timerView.dto;

import org.morib.server.domain.task.infra.Task;
import org.morib.server.domain.timer.infra.TimerStatus;

import java.time.LocalDate;

public record UpdateTimerSessionDto(
        String runningCategoryName,
        Task selectedTask,
        int elapsedTime,
        TimerStatus timerStatus,
        LocalDate targetDate
) {
    public static UpdateTimerSessionDto of(String runningCategoryName, Task selectedTask, int elapsedTime, TimerStatus timerStatus, LocalDate targetDate) {
        return new UpdateTimerSessionDto(runningCategoryName, selectedTask, elapsedTime, timerStatus, targetDate);
    }
}
