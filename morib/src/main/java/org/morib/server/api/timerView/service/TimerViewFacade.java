package org.morib.server.api.timerView.service;

import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.morib.server.annotation.Facade;
import org.morib.server.api.timerView.dto.StopTimerRequestDto;
import org.morib.server.api.timerView.dto.TaskInTodoCardDto;
import org.morib.server.api.timerView.service.fetch.task.FetchTaskService;
import org.morib.server.api.timerView.service.fetch.timer.FetchTimerService;
import org.morib.server.api.timerView.service.fetch.user.FetchUserService;
import org.morib.server.domain.category.infra.Category;
import org.morib.server.domain.task.infra.Task;
import org.morib.server.domain.timer.infra.Timer;
import org.morib.server.domain.user.infra.User;

@Facade
@RequiredArgsConstructor
public class TimerViewFacade {

    private final FetchTimerService fetchTimerService;
    private final FetchUserService fetchUserService;
    private final FetchTaskService fetchTaskService;

    /**
     * timer 가 멈추면 계산을 한다.
     * <p>
     * user를 받고 그 유저의 task들중의 id 에 맞는 친구를 꺼내고 그 task의 timer들 중 targetDate 랑 같은 친구를 찾고
     * <p>
     * 그 timer의 elapsedTime 에 param으로 받은 elapsedTime을 더한다.
     */
    @Transactional
    public void afterStopTimer(Long taskId, StopTimerRequestDto dto) {
        Long mockUserId = 1L;
        User user = fetchUserService.fetchByUserId(mockUserId);
        Set<Category> categories = user.getCategories();
        Task findTask = fetchTaskService.fetchByTaskIdInCategories(categories, taskId);
        Timer timer = fetchTimerService.fetchByTaskAndTargetDate(findTask, dto.targetDate());
        fetchTimerService.addElapsedTime(timer, dto.elapsedTime());
    }


    /**
     * todoCard를 가져온다. 이전 코드의 로직을 참고하여 진행한다.
     *
     * @param targetDate
     */
    public TaskInTodoCardDto getTodoCard(LocalDate targetDate) {
        return null;
    }


}
