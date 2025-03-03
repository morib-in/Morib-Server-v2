package org.morib.server.domain.todo.application;

import org.morib.server.domain.todo.infra.Todo;

import java.time.LocalDate;
import java.util.Optional;

public interface FetchTodoService {
    Optional<Todo> fetchOrNullByUserIdAndTargetDate(Long mockUserId, LocalDate targetDate);
    Todo fetchByUserIdAndTargetDate(Long userId, LocalDate targetDate);
}
