package org.morib.server.domain.task.application;

import org.morib.server.api.homeView.vo.TaskWithTimers;
import org.morib.server.domain.task.infra.Task;
import org.morib.server.domain.todo.infra.Todo;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;

public interface FetchTaskService {
    Task fetchById(Long taskId);
    Task fetchByIdAndTimer(Long taskId);
    LinkedHashSet<Task> fetchByTodoAndSameTargetDate(Todo todo, LocalDate targetDate);
    TaskWithTimers convertToTaskWithTimers(Task task);
    List<Task> fetchByTaskIds(List<Long> id);
}
