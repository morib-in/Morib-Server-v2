package org.morib.server.domain.todo.application;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.morib.server.domain.todo.infra.Todo;
import org.morib.server.domain.todo.infra.TodoRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FetchTodoServiceImpl implements FetchTodoService {

    private final TodoRepository todoRepository;

    @Override
    public void fetch(LocalDate targetDate) {

    }

    @Override
    public Todo fetchByUserIdAndTargetDate(Long userId, LocalDate targetDate) {
        return todoRepository.findTodoByUserIdAndTargetDate(userId, targetDate)
            .orElseThrow(() -> new IllegalArgumentException("해당 유저의 todo가 없습니다."));
    }

    @Override
    public Todo fetchByUserId(Long userId) {
        return todoRepository.findTodoByUserId(userId)
            .orElseThrow(() -> new IllegalArgumentException("해당 유저의 todo가 없습니다."));
    }
}
