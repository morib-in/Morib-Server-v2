package org.morib.server.api.timerView.facade;

import java.time.LocalDate;
import org.morib.server.api.timerView.dto.TodoCardResponseDto;

public interface FetchTodoFacade {
    TodoCardResponseDto fetchTodoCard(LocalDate targetDate);
}
