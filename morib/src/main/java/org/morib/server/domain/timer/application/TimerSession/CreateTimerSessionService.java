package org.morib.server.domain.timer.application.TimerSession;

import org.morib.server.domain.timer.infra.TimerStatus;

import java.time.LocalDate;

public interface CreateTimerSessionService {
    void create(Long userId, String runningCategoryName, Long taskId, int elapsedTime, TimerStatus timerStatus, LocalDate targetDate);
}
