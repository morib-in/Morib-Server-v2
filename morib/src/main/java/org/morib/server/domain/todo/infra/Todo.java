package org.morib.server.domain.todo.infra;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.morib.server.domain.task.infra.Task;
import org.morib.server.domain.user.infra.User;
import org.morib.server.global.common.BaseTimeEntity;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Todo extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "todo_id")
    private Long id;
    @Column(nullable = false)
    private LocalDate targetDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany
    @JoinTable(
            name = "todo_task",
            joinColumns = @JoinColumn(name = "todo_id"),
            inverseJoinColumns = @JoinColumn(name = "task_id")
    )
    private Set<Task> tasks = new LinkedHashSet<>();

    @Builder
    public Todo(LocalDate targetDate, User user) {
        this.targetDate = targetDate;
        this.user = user;
    }

    public static Todo createByTargetDateAndUser(LocalDate targetDate, User user) {
        return Todo.builder().
            targetDate(targetDate).
            user(user)
            .build();
    }

    public void updateTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }

}
