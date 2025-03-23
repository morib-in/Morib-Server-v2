package org.morib.server.domain.timer.infra;

import jakarta.persistence.*;
import lombok.*;
import org.morib.server.global.common.BaseTimeEntity;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TimerSession extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "timer_session_id")
    private Long id;
    @Column(nullable = false)
    private Long userId;
    @Column(nullable = false)
    private String runningCategoryName;
    @Column(nullable = false)
    private Long taskId;
    @Column(nullable = false)
    private int elapsedTime;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TimerStatus timerStatus;
    @Column(nullable = false)
    LocalDate targetDate;

    public static TimerSession create(Long userId, String runningCategoryName, Long taskId, int elapsedTime, TimerStatus timerStatus, LocalDate targetDate) {
        return TimerSession.builder()
                .userId(userId)
                .runningCategoryName(runningCategoryName)
                .taskId(taskId)
                .elapsedTime(elapsedTime)
                .timerStatus(timerStatus)
                .targetDate(targetDate)
                .build();
    }
}
