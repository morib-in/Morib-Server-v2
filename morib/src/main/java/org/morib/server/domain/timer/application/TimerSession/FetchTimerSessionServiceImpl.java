package org.morib.server.domain.timer.application.TimerSession;

import lombok.RequiredArgsConstructor;
import org.morib.server.domain.timer.infra.TimerSession;
import org.morib.server.domain.timer.infra.TimerSessionRepository;
import org.morib.server.domain.timer.infra.TimerStatus;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FetchTimerSessionServiceImpl implements FetchTimerSessionService {

    private final TimerSessionRepository timerSessionRepository;

    @Override
    public TimerSession fetchTimerSession(Long userId, LocalDate targetDate) {
        return timerSessionRepository.findByUserIdAndTargetDate(userId, targetDate);
    }

    @Override
    public List<TimerSession> fetchExpiredTimerSessions(LocalDateTime targetDateTime) {
        return timerSessionRepository.findByTimerStatusAndLastHeartbeatAtBefore(TimerStatus.RUNNING, targetDateTime);
    }
}
