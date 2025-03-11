package org.morib.server.domain.task.application;

import org.morib.server.domain.task.infra.Task;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public interface ClassifyTaskService {
    LinkedHashSet<Task> sortTasksByCreatedAt(Set<Task> tasks);
    boolean isTaskInDateRange(Task task, LocalDate date);
    void validateIncludingCompletedTasks(List<Task> tasks);
    void validateIncludingCompletedTask(Task task);
}
