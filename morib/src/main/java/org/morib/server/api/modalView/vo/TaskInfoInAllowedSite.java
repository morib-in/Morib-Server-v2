package org.morib.server.api.modalView.vo;

import org.morib.server.domain.task.infra.Task;

public record TaskInfoInAllowedSite(
        Long id,
        String name
) {
    public static TaskInfoInAllowedSite of(Task task) {
        return new TaskInfoInAllowedSite(task.getId(), task.getName());
    }
}
