package org.morib.server.api.timerView.service.fetch.task;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;
import org.morib.server.domain.category.infra.Category;
import org.morib.server.domain.task.infra.Task;
import org.morib.server.domain.todo.infra.Todo;

public interface FetchTaskService {
    void fetch();

    Task fetchByTaskIdInCategories(Set<Category> categories, Long taskId);

    LinkedHashSet<Task> fetchByTodoAndSameTargetDate(Todo todo, LocalDate targetDate);
}
