package org.morib.server.domain.timer.application.TimerSession;

import org.morib.server.domain.timer.infra.TimerSession;

import java.time.LocalDateTime;

public interface CalculateTimerSessionService {
    int calculateElapsedTimeByLastCalculatedAt(TimerSession timerSession, LocalDateTime now);
}
