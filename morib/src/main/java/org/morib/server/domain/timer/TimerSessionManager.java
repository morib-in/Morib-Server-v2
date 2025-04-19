package org.morib.server.domain.timer;

import org.morib.server.annotation.Manager;
import org.morib.server.api.timerView.dto.SaveTimerSessionRequestDto;
import org.morib.server.domain.timer.infra.TimerSession;

@Manager
public class TimerSessionManager {

    public void updateTimerSession(TimerSession timerSession, SaveTimerSessionRequestDto saveTimerSessionRequestDto) {
        timerSession.setElapsedTime(saveTimerSessionRequestDto.elapsedTime());
        timerSession.setTimerStatus(saveTimerSessionRequestDto.timerStatus());
        timerSession.setTaskId(saveTimerSessionRequestDto.taskId());
    public void run(TimerSession timerSession, LocalDateTime now) {
        timerSession.run(now);
    }

    public void pause(int calculatedElapsedTime, TimerSession timerSession, LocalDateTime now) {
        timerSession.pause(calculatedElapsedTime, now);
    }
    public TimerSession handleCalledByClientFetch(int calculatedElapsedTime, TimerSession timerSession, LocalDateTime now) {
        return timerSession.handleCalledByClientFetch(calculatedElapsedTime, now);
    }

}
