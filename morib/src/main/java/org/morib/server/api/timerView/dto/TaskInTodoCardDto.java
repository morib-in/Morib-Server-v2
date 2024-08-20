package org.morib.server.api.timerView.dto;

import java.time.LocalDate;

public record TaskInTodoCardDto(
    Long id,
    String categoryName,
    String name,
    LocalDate startDate,
    LocalDate endDate,
    boolean isComplete,
    LocalDate targetDate,
    int targetTime,
    int taskOrder
) {
}
