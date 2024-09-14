package org.morib.server.api.modalView.vo;

import java.time.LocalDate;
import org.morib.server.domain.task.infra.Task;

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
