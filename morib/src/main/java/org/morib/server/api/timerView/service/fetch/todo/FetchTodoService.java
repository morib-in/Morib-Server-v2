package org.morib.server.api.timerView.service.fetch.todo;

import org.morib.server.domain.todo.infra.Todo;

import java.time.LocalDate;

public interface FetchTodoService {
    void fetch(LocalDate targetDate);
    Todo fetchByUserIdAndTargetDate(Long mockUserId, LocalDate targetDate);
    Todo fetchByUserId(Long userId);
}
