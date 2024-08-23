package org.morib.server.api.timerView.facade;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.morib.server.annotation.Facade;
import org.morib.server.api.timerView.dto.StopTimerRequestDto;
import org.morib.server.domain.task.application.FetchTaskService;
import org.morib.server.domain.task.infra.Task;
import org.morib.server.domain.timer.application.TimeManager;
import org.morib.server.domain.timer.application.FetchTimerService;
import org.morib.server.domain.timer.infra.Timer;


@Facade
@RequiredArgsConstructor
public class TimerViewFacade {

    private final FetchTimerService fetchTimerService;
    private final FetchTaskService fetchTaskService;
    private final TimeManager timeManager;

    @Transactional
    public void stopAfterSumElapsedTime(Long taskId, StopTimerRequestDto dto) {
    //  Long mockUserId = 1L;
    //  User user = fetchUserService.fetchByUserId(mockUserId);   이후에 user 관련해서 여쭤보고 진행!
        Task findTask = fetchTaskService.fetchById(taskId);
        Timer timer = fetchTimerService.fetchByTaskAndTargetDate(findTask, dto.targetDate());
        timeManager.addElapsedTime(timer, dto.elapsedTime());
    }


}
