package org.morib.server.domain.timer.application.TimerSession;

import org.morib.server.domain.task.infra.Task;
import org.morib.server.domain.timer.infra.TimerSession;
import org.morib.server.domain.timer.infra.TimerStatus;

import java.time.LocalDate;

public interface CreateTimerSessionService {
    TimerSession create(Long userId, String runningCategoryName, Task task, int elapsedTime, TimerStatus timerStatus, LocalDate targetDate);
}
