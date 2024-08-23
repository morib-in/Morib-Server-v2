package org.morib.server.domain.todo.application;

import java.time.LocalDate;

public interface FetchTodoService {
    void fetch(LocalDate targetDate);
}
