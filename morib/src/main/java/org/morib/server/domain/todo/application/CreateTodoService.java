package org.morib.server.domain.todo.application;

import java.time.LocalDate;
import org.morib.server.domain.todo.infra.Todo;
import org.morib.server.domain.user.infra.User;

public interface CreateTodoService {
    Todo saveTodoByUserAndTargetDate(User user, LocalDate targetDate);
}
