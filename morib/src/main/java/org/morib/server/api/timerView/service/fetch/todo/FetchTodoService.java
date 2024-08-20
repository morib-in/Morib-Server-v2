package org.morib.server.api.timerView.service.fetch.todo;

import java.time.LocalDate;

public interface FetchTodoService {
    void fetch(LocalDate targetDate);
}
