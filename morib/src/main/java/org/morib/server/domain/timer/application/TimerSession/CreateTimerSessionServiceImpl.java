package org.morib.server.domain.timer.application.TimerSession;

import lombok.RequiredArgsConstructor;
import org.morib.server.domain.timer.infra.TimerSession;
import org.morib.server.domain.timer.infra.TimerSessionRepository;
import org.morib.server.domain.timer.infra.TimerStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class CreateTimerSessionServiceImpl implements CreateTimerSessionService {

    private final TimerSessionRepository timerSessionRepository;

    @Override
    public void create(Long userId, String runningCategoryName, Long taskId, int elapsedTime, TimerStatus timerStatus, LocalDate targetDate) {
        timerSessionRepository.save(
                TimerSession.create(userId, runningCategoryName, taskId, elapsedTime, timerStatus, targetDate));
    }
}
