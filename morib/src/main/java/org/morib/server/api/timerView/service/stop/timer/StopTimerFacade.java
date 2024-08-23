package org.morib.server.api.timerView.service.stop.timer;

import org.morib.server.api.timerView.dto.StopTimerRequestDto;

public interface StopTimerFacade {

 void afterStopTimer(Long taskId, StopTimerRequestDto dto);
}
