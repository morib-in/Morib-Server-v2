package org.morib.server.domain.todo.application;


import org.morib.server.domain.todo.infra.Todo;

import java.time.LocalDate;

public interface TodoGateway {

    void save();

    void findById();

    Todo findTodoByUserId(Long userId);

    void findAll();

    void deleteById();

    void deleteAll();

    Todo findTodoByUserIdAndTargetDate(Long userId, LocalDate targetDate);

}
