package org.morib.server.api.homeView.dto.fetch;

import org.morib.server.domain.task.infra.Task;

import java.time.LocalDate;

public record FetchTaskDto(
        Long id,
        String name,
        LocalDate startDate,
        LocalDate endDate,
        int targetTime,
        boolean isComplete
) {
    public static FetchTaskDto of(Task task, int elapsedTime) {
        return new FetchTaskDto(
                task.getId(),
                task.getName(),
                task.getStartDate(),
                task.getEndDate(),
                elapsedTime,
                task.getIsComplete());
    }
}

