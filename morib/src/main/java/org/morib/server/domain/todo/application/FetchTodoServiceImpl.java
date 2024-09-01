package org.morib.server.domain.todo.application;

import java.time.LocalDate;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.morib.server.domain.todo.infra.Todo;
import org.morib.server.domain.todo.infra.TodoRepository;
import org.morib.server.global.exception.NotFoundException;
import org.morib.server.global.message.ErrorMessage;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FetchTodoServiceImpl implements FetchTodoService {

    private final TodoRepository todoRepository;

    @Override
    public Optional<Todo> fetchOrNullByUserIdAndTargetDate(Long userId, LocalDate targetDate) {
        return todoRepository.findTodoByUserIdAndTargetDate(userId, targetDate);
    }

    @Override
    public Todo fetchByUserIdAndTargetDate(Long mockUserId, LocalDate targetDate) {
        return todoRepository.findTodoByUserIdAndTargetDate(mockUserId, targetDate)
            .orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND));
    }

}
