package org.morib.server.domain.timer.application.TimerSession;

import lombok.RequiredArgsConstructor;
import org.morib.server.domain.timer.infra.TimerSession;
import org.morib.server.domain.timer.infra.TimerSessionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class FetchTimerSessionServiceImpl implements FetchTimerSessionService {

    private final TimerSessionRepository timerSessionRepository;

    @Override
    public TimerSession fetchTimerSession(Long userId, LocalDate targetDate) {
        return timerSessionRepository.findByUserIdAndTargetDate(userId, targetDate);
    }

}
