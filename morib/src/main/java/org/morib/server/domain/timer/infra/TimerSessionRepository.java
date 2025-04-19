package org.morib.server.domain.timer.infra;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TimerSessionRepository extends JpaRepository<TimerSession, Long> {
    TimerSession findByUserIdAndTargetDate(Long userId, LocalDate targetDate);
    List<TimerSession> findByTimerStatusAndLastHeartbeatAtBefore(TimerStatus timerStatus, LocalDateTime lastHeartbeatAt);
    List<TimerSession> findByTimerStatus(TimerStatus timerStatus);
}
