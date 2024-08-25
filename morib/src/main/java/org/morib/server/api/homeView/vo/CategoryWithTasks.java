package org.morib.server.api.homeView.vo;

import org.morib.server.domain.category.infra.Category;

import java.util.LinkedHashSet;

public record CategoryWithTasks(
        Category category,
        LinkedHashSet<TaskWithTimers> taskWithTimers
) {
    public static CategoryWithTasks of(Category category, LinkedHashSet<TaskWithTimers> taskWithTimers) {
        return new CategoryWithTasks(category, taskWithTimers);
    }
}
