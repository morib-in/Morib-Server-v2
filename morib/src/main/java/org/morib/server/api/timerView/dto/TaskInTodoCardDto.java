package org.morib.server.api.timerView.dto;

import org.morib.server.domain.task.infra.Task;

import java.time.LocalDate;

public record TaskInTodoCardDto(
    String name,
    LocalDate startDate,
    LocalDate endDate,
    boolean isComplete,
    LocalDate targetDate,
    int targetTime
) {

    public static TaskInTodoCardDto of(Task task, LocalDate targetDate, int elapsedTime){
        return new TaskInTodoCardDto(task.getName(), task.getStartDate(), task.getEndDate(), task.getIsComplete(), targetDate, elapsedTime);
    }
}
