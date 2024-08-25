package org.morib.server.domain.task.application;

import java.time.LocalDate;
import java.util.LinkedHashSet;

import org.morib.server.api.homeView.vo.TaskWithTimers;
import org.morib.server.domain.task.infra.Task;
import org.morib.server.domain.todo.infra.Todo;

public interface FetchTaskService {
    void fetch();

    Task fetchById(Long taskId);

    LinkedHashSet<Task> fetchByTodoAndSameTargetDate(Todo todo, LocalDate targetDate);

    TaskWithTimers convertToTaskWithTimers(Task task);
}
