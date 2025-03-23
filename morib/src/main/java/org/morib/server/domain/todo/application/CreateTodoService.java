package org.morib.server.domain.todo.application;

import org.morib.server.domain.todo.infra.Todo;
import org.morib.server.domain.user.infra.User;

import java.time.LocalDate;

public interface CreateTodoService {
    Todo saveTodoByUserAndTargetDate(User user, LocalDate targetDate);
}
