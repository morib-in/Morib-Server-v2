package org.morib.server.api.timerView.service.fetch.todo;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.morib.server.domain.todo.application.TodoGateway;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FetchTodoServiceImpl implements FetchTodoService {

    private final TodoGateway todoGateway;

    @Override
    public void fetch(LocalDate targetDate) {

    }
}
