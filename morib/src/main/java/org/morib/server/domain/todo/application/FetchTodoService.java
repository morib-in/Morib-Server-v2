package org.morib.server.domain.todo.application;

import org.morib.server.domain.todo.infra.Todo;

import java.time.LocalDate;
import java.util.Optional;

public interface FetchTodoService {
    Optional<Todo> fetchByUserIdAndTargetDate(Long mockUserId, LocalDate targetDate);
    Todo fetchByUserIdAndTargetDateNotNull(Long mockUserId, LocalDate targetDate);
}
