package org.morib.server.api.homeViewApi.vo;

import lombok.Value;
import org.morib.server.domain.category.infra.Category;
import org.morib.server.domain.task.infra.Task;

import java.util.List;

@Value
public class TasksByCategory {
    Category category;
    List<Task> tasks;

    public static TasksByCategory of(Category category, List<Task> tasks) {
        return new TasksByCategory(category, tasks);
    }
}
