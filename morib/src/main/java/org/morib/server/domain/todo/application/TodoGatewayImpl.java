package org.morib.server.domain.todo.application;

import lombok.RequiredArgsConstructor;
import org.morib.server.domain.todo.infra.Todo;
import org.morib.server.domain.todo.infra.TodoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class TodoGatewayImpl implements TodoGateway {

    private final TodoRepository todoRepository;

    @Override
    public void save() {

    }

    @Override
    public void findById() {

    }

    @Override
    public Todo findTodoByUserId(Long userId) {
        return todoRepository.findTodoByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저에 해당하는 todo가 없습니다."));
    }

    @Override
    public void findAll() {

    }

    @Override
    public void deleteById() {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public Todo findTodoByUserIdAndTargetDate(Long userId, LocalDate targetDate) {
        return todoRepository.findTodoByUserIdAndTargetDate(userId,targetDate)
                .orElseThrow(() -> new IllegalArgumentException("유저에 해당하는 todo가 없습니다."));
    }
}
