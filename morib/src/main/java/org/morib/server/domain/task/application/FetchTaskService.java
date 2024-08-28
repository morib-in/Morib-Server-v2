package org.morib.server.domain.task.application;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.morib.server.domain.task.infra.Task;
import org.morib.server.domain.todo.infra.Todo;
import org.morib.server.domain.user.infra.User;

public interface FetchTaskService {
    void fetch();

    Task fetchById(Long taskId);

    LinkedHashSet<Task> fetchByTodoAndSameTargetDate(Todo todo, LocalDate targetDate);

    Set<Task> fetchByTaskIds(List<Long> id);

    Task fetchByUserAndTaskId(User findUser, Long taskId);
}
