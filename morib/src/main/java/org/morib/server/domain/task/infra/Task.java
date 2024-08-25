package org.morib.server.domain.task.infra;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.morib.server.domain.category.infra.Category;
import org.morib.server.domain.timer.infra.Timer;
import org.morib.server.domain.todo.infra.Todo;
import org.morib.server.global.common.BaseTimeEntity;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Task extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private Boolean isComplete;
    @Column(nullable = false)
    private LocalDate startDate;
    private LocalDate endDate;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Timer> timers;

    @ManyToMany(mappedBy = "tasks", cascade = CascadeType.REMOVE)
    private Set<Todo> todos;

    @Builder
    public Task (String name, Boolean isComplete, LocalDate startDate, LocalDate endDate, Category category) {
        this.name = name;
        this.isComplete = isComplete;
        this.startDate = startDate;
        this.endDate = endDate;
        this.category = category;
    }

    public  static Task createTask(String name, Boolean isComplete, LocalDate startDate, LocalDate endDate, Category category) {
        return Task.builder()
                .name(name)
                .isComplete(isComplete)
                .startDate(startDate)
                .endDate(endDate)
                .category(category)
                .build();
    }

}
