package org.morib.server.api.homeViewApi.dto.fetch;

import org.morib.server.domain.category.infra.Category;
import org.morib.server.domain.task.infra.Task;
import org.morib.server.domain.timer.infra.Timer;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

