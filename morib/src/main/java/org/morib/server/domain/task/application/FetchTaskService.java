package org.morib.server.domain.task.application;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import org.morib.server.api.homeView.vo.TaskWithTimers;
import java.util.List;
import java.util.Set;

import org.morib.server.domain.category.infra.Category;
import org.morib.server.domain.task.infra.Task;
import org.morib.server.domain.todo.infra.Todo;

public interface FetchTaskService {
    Task fetchById(Long taskId);
    Task fetchByIdAndTimer(Long taskId);
    LinkedHashSet<Task> fetchByTodoAndSameTargetDate(Todo todo, LocalDate targetDate);
    TaskWithTimers convertToTaskWithTimers(Task task);
    List<Task> fetchByTaskIds(List<Long> id);
}
