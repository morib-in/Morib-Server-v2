package org.morib.server.api.homeView.vo;

import org.morib.server.domain.task.infra.Task;

import java.time.LocalDate;

public record TaskInfo(
        Long id,
        String name,
        LocalDate startDate,
        LocalDate endDate,
        int elapsedTime,
        boolean isComplete
) {
    public static TaskInfo of(Task task, int elapsedTime) {
        return new TaskInfo(
                task.getId(),
                task.getName(),
                task.getStartDate(),
                task.getEndDate(),
                elapsedTime,
                task.getIsComplete());
    }
}

