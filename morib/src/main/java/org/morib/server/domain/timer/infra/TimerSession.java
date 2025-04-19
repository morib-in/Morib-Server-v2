package org.morib.server.domain.timer.infra;

import jakarta.persistence.*;
import lombok.*;
import org.morib.server.domain.task.infra.Task;
import org.morib.server.global.common.BaseTimeEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "timer_session",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "targetDate"})})
public class TimerSession extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "timer_session_id")
    private Long id;
    @Column(nullable = false)
    private Long userId;
    @Column(nullable = false)
    private String runningCategoryName;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "selected_task_id")
    private Task selectedTask;
    @Column(nullable = false)
    private int elapsedTime;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TimerStatus timerStatus = TimerStatus.PAUSED;
    @Column(nullable = false)
    private LocalDate targetDate;
    private LocalDateTime lastCalculatedAt;
    private LocalDateTime lastHeartbeatAt;

    public static TimerSession create(Long userId, String runningCategoryName, Task selectedTask, int elapsedTime, TimerStatus timerStatus, LocalDate targetDate) {
        return TimerSession.builder()
                .userId(userId)
                .runningCategoryName(runningCategoryName)
                .selectedTask(selectedTask)
                .elapsedTime(elapsedTime)
                .timerStatus(timerStatus)
                .targetDate(targetDate)
                .build();
    }

    public void run(LocalDateTime now) {
        this.timerStatus = TimerStatus.RUNNING;
        this.lastCalculatedAt = now;
        this.lastHeartbeatAt = now;
    }

    public void pause(int calculateElapsedTime, LocalDateTime now) {
        if (this.timerStatus != TimerStatus.RUNNING || this.lastCalculatedAt == null) return; // 실행 중이 아닐 때 무시
        this.elapsedTime += calculateElapsedTime;
        this.timerStatus = TimerStatus.PAUSED;
        this.lastCalculatedAt = now;
        this.lastHeartbeatAt = now;
    }

    public void updateHeartbeat(LocalDateTime now) {
        if (this.timerStatus != TimerStatus.RUNNING) return;
        this.lastHeartbeatAt = now;
    }

    public void update(String runningCategoryName, Task selectedTask, int elapsedTime, TimerStatus timerStatus, LocalDate targetDate) {
        this.runningCategoryName = runningCategoryName;
        this.selectedTask = selectedTask;
        this.elapsedTime = elapsedTime;
        this.timerStatus = timerStatus;
        this.targetDate = targetDate;
        this.lastCalculatedAt = null;
        this.lastHeartbeatAt = null;
    }

    public TimerSession handleCalledByClientFetch(int calculatedElapsedTime, LocalDateTime now) {
        this.elapsedTime += calculatedElapsedTime;
        this.lastCalculatedAt = now;
        this.lastHeartbeatAt = now;
        return this;
    }

}

