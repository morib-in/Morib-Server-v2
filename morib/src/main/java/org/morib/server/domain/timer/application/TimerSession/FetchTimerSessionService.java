package org.morib.server.domain.timer.application.TimerSession;

import org.morib.server.domain.timer.infra.TimerSession;

import java.time.LocalDate;

public interface FetchTimerSessionService {
    TimerSession fetchTimerSession(Long userId, LocalDate targetDate);
}
