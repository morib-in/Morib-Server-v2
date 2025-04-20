package org.morib.server.domain.timer;

import org.morib.server.annotation.Manager;
import org.morib.server.api.timerView.dto.UpdateTimerSessionDto;
import org.morib.server.domain.task.infra.Task;
import org.morib.server.domain.timer.infra.TimerSession;
import org.morib.server.domain.timer.infra.TimerStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Manager
public class TimerSessionManager {

    public void updateTimerSession(TimerSession timerSession, UpdateTimerSessionDto updateTimerSessionDto) {
        timerSession.update(
                updateTimerSessionDto.runningCategoryName(),
                updateTimerSessionDto.selectedTask(),
                updateTimerSessionDto.elapsedTime(),
                updateTimerSessionDto.timerStatus(),
                updateTimerSessionDto.targetDate()
        );
    }

    public void run(TimerSession timerSession, LocalDateTime now) {
        timerSession.run(now);
    }

    public void pause(int calculatedElapsedTime, TimerSession timerSession, LocalDateTime now) {
        timerSession.pause(calculatedElapsedTime, now);
    }

    public void handleHeartbeat(TimerSession timerSession, LocalDateTime now) {
        timerSession.updateHeartbeat(now);
    }

    public TimerSession handleCalledByClientFetch(int calculatedElapsedTime, TimerSession timerSession, LocalDateTime now) {
        return timerSession.handleCalledByClientFetch(calculatedElapsedTime, now);
    }

}
