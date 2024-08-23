package org.morib.server.api.timerView.service.fetch.todo;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.morib.server.domain.todo.application.FetchTodoService;
import org.morib.server.domain.todo.application.TodoGateway;
import org.morib.server.domain.todo.infra.Todo;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FetchTodoServiceImpl implements FetchTodoService {

    private final TodoGateway todoGateway;

    @Override
    public void fetch(LocalDate targetDate) {

    }

    @Override
    public Todo fetchByUserIdAndTargetDate(Long userId, LocalDate targetDate) {
        return todoGateway.findTodoByUserIdAndTargetDate(userId, targetDate);
    }

    @Override
    public Todo fetchByUserId(Long userId) {
        return todoGateway.findTodoByUserId(userId);
    }
}
