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
        timerSession.setTargetDate(saveTimerSessionRequestDto.targetDate());
    }
}
