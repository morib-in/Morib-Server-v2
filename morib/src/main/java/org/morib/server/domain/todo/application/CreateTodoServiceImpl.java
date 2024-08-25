package org.morib.server.domain.todo.application;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.morib.server.domain.todo.infra.Todo;
import org.morib.server.domain.todo.infra.TodoRepository;
import org.morib.server.domain.user.infra.User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateTodoServiceImpl implements CreateTodoService {

    private final TodoRepository todoRepository;

    @Override
    public Todo saveTodoByTargetDateAndUser(LocalDate targetDate, User user) {
       return todoRepository.save(buildTodoByTargetDateAndUser(targetDate, user));
    }

    private Todo buildTodoByTargetDateAndUser(LocalDate targetDate, User user) {
        return Todo.builder()
            .user(user)
            .targetDate(targetDate)
            .build();
    }
}
