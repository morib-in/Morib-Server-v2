package org.morib.server.api.timerView.dto;

import org.morib.server.domain.task.infra.Task;

import java.time.LocalDate;

public record TaskInTodoCardDto(
    Long id,
    String name,
    String categoryName,
    LocalDate startDate,
    LocalDate endDate,
    boolean isComplete,
    LocalDate targetDate,
    int elapsedTime
) {

    public static TaskInTodoCardDto of(Task task, LocalDate targetDate, int elapsedTime){
        return new TaskInTodoCardDto(task.getId(), task.getName(), task.getCategory().getName(), task.getStartDate(), task.getEndDate(), task.getIsComplete(), targetDate, elapsedTime);
    }
}
