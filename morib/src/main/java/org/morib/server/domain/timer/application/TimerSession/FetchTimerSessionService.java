package org.morib.server.domain.timer.application.TimerSession;

import org.morib.server.domain.timer.infra.TimerSession;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface FetchTimerSessionService {
    TimerSession fetchTimerSession(Long userId, LocalDate targetDate);
    List<TimerSession> fetchExpiredTimerSessions(LocalDateTime targetDateTime);
}
