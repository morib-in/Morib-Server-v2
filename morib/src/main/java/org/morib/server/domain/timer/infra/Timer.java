package org.morib.server.domain.timer.infra;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.morib.server.domain.category.infra.Category;
import org.morib.server.domain.task.infra.Task;

import java.time.LocalDate;
import org.morib.server.domain.user.infra.User;
import org.morib.server.global.common.BaseTimeEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Timer extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "timer_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @Column(nullable = false)
    private LocalDate targetDate;
    @Column(nullable = false)
    private int elapsedTime;
    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    public void addElapsedTime(int i) {
        this.elapsedTime += i;
    }
}
