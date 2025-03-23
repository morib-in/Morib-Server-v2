package org.morib.server.api.modalView.vo;

import org.morib.server.domain.task.infra.Task;

import java.time.LocalDate;

public record TaskInfoInAllowedSite(
        Long id,
        String name,
        LocalDate startDate,
        LocalDate endDate
) {
    public static TaskInfoInAllowedSite of(Task task) {
        return new TaskInfoInAllowedSite(task.getId(), task.getName(), task.getStartDate(), task.getEndDate());
    }
}
