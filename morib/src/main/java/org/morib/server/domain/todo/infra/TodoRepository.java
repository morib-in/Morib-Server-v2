package org.morib.server.domain.todo.infra;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    Optional<Todo> findTodoByUserId(Long userId);

    Optional<Todo> findTodoByUserIdAndTargetDate(Long userId, LocalDate targetDate);
}
