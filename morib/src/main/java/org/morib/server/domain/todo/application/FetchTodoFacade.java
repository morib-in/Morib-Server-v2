package org.morib.server.api.timerView.service.fetch.todo;

import org.morib.server.api.timerView.dto.TodoCardResponseDto;

import java.time.LocalDate;

public interface FetchTodoFacade {
    TodoCardResponseDto fetchTodoCard(LocalDate targetDate);
}
