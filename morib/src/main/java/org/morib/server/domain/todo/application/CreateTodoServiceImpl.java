package org.morib.server.domain.todo.application;

import lombok.RequiredArgsConstructor;
import org.morib.server.domain.todo.infra.Todo;
import org.morib.server.domain.todo.infra.TodoRepository;
import org.morib.server.domain.user.infra.User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class CreateTodoServiceImpl implements CreateTodoService {

    private final TodoRepository todoRepository;

    @Override
    public Todo saveTodoByUserAndTargetDate(User user, LocalDate targetDate) {
        return todoRepository.save(Todo.createByTargetDateAndUser(targetDate,user));
    }
}
