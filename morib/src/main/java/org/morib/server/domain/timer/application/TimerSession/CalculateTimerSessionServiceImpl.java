package org.morib.server.domain.timer.application.TimerSession;

import lombok.RequiredArgsConstructor;
import org.morib.server.domain.timer.infra.TimerSession;
import org.morib.server.domain.timer.infra.TimerSessionRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CalculateTimerSessionServiceImpl implements CalculateTimerSessionService {

    private final TimerSessionRepository timerSessionRepository;

    @Override
    public int calculateElapsedTimeByLastCalculatedAt(TimerSession timerSession, LocalDateTime now) {
        Duration elapsedSinceLastStart = Duration.between(timerSession.getLastCalculatedAt(), now);
        return (int)elapsedSinceLastStart.toSeconds();
    }

}
