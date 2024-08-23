package org.morib.server.api.timerView.service.stop.timer;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.morib.server.annotation.Facade;
import org.morib.server.api.timerView.dto.StopTimerRequestDto;
import org.morib.server.api.timerView.service.fetch.task.FetchTaskService;
import org.morib.server.api.timerView.service.fetch.timer.FetchTimerService;
import org.morib.server.api.timerView.service.fetch.user.FetchUserService;
import org.morib.server.domain.category.infra.Category;
import org.morib.server.domain.task.infra.Task;
import org.morib.server.domain.timer.TimerOperator;
import org.morib.server.domain.timer.infra.Timer;
import org.morib.server.domain.user.infra.User;
import org.springframework.stereotype.Service;

import java.util.Set;

@Facade
@RequiredArgsConstructor
public class StopTimerFacadeImpl implements StopTimerFacade {

    private final FetchTimerService fetchTimerService;
    private final FetchUserService fetchUserService;
    private final FetchTaskService fetchTaskService;

    @Override
    @Transactional
    public void afterStopTimer(Long taskId, StopTimerRequestDto dto) {
        Long mockUserId = 1L;
        User user = fetchUserService.fetchByUserId(mockUserId);
        Set<Category> categories = user.getCategories();
        Task findTask = fetchTaskService.fetchByTaskIdInCategories(categories, taskId);
        Timer timer = fetchTimerService.fetchByTaskAndTargetDate(findTask, dto.targetDate());
        fetchTimerService.addElapsedTime(timer, dto.elapsedTime());
    }
}
