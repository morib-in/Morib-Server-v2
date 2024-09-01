package org.morib.server.api.timerView.facade;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.morib.server.annotation.Facade;
import org.morib.server.api.timerView.dto.StopTimerRequestDto;
import org.morib.server.api.timerView.dto.TaskInTodoCardDto;
import org.morib.server.api.timerView.dto.TodoCardResponseDto;
import org.morib.server.domain.task.application.FetchTaskService;
import org.morib.server.domain.task.infra.Task;
import org.morib.server.domain.timer.TimerManager;
import org.morib.server.domain.timer.application.FetchTimerService;
import org.morib.server.domain.timer.infra.Timer;
import org.morib.server.domain.todo.application.FetchTodoService;
import org.morib.server.domain.todo.infra.Todo;
import org.morib.server.domain.user.application.FetchUserService;
import org.morib.server.global.exception.BusinessException;
import org.morib.server.global.message.ErrorMessage;
import org.springframework.transaction.annotation.Transactional;


@Facade
@RequiredArgsConstructor
public class TimerViewFacade {

    private final FetchTimerService fetchTimerService;
    private final FetchTaskService fetchTaskService;
    private final FetchTodoService fetchTodoService;
    private final FetchUserService fetchUserService;
    private final TimerManager timerManager;

    @Transactional
    public void stopAfterSumElapsedTime(Long taskId, StopTimerRequestDto dto) {
    //  Long mockUserId = 1L;
    //  User user = fetchUserService.fetchByUserId(mockUserId);   이후에 user 관련해서 여쭤보고 진행!
        Task findTask = fetchTaskService.fetchById(taskId);
        Timer timer = fetchTimerService.fetchByTaskAndTargetDate(findTask, dto.targetDate());
        timerManager.addElapsedTime(timer, dto.elapsedTime());
    }


    /**
     *
     * 타이머 뷰 안에서 todo 내의 task 들을 가져온다.
     * 해당 타이머들의 총 시간을 계산해야함!
     * 1. todo를 유저를 통해 찾는다.
     * 2. 찾은 todo 에서 task들을 불러온다.
     * 3. totalTimeToday~ 는 유저id랑, targetDate를 바탕으로 user의 모든 timer를 조회해서 elapsedTime을 더한 값이다.
     * @param targetDate
     * @return
     */
    @Transactional
    public TodoCardResponseDto fetchTodoCard(Long mockUserId, LocalDate targetDate) {
            Todo todo = fetchTodoService.fetchByUserIdAndTargetDate(mockUserId, targetDate)
            .orElseThrow(() -> new BusinessException(ErrorMessage.NOT_FOUND));

        LinkedHashSet<Task> tasks = fetchTaskService.fetchByTodoAndSameTargetDate(todo, targetDate);
        int totalTimeOfToday = fetchTimerService.sumElapsedTimeByUser(fetchUserService.fetchByUserId(mockUserId), targetDate);

        List<TaskInTodoCardDto> taskInTodoCardDtos = tasks.stream()
            .map(t -> getTaskInTodoCardDto(targetDate, t))
            .toList();

        return new TodoCardResponseDto(totalTimeOfToday, taskInTodoCardDtos);
    }

    private TaskInTodoCardDto getTaskInTodoCardDto(LocalDate targetDate, Task task) {
        return TaskInTodoCardDto.of(task, targetDate, fetchTimerService.sumOneTaskElapsedTimeInTargetDate(task, targetDate));
    }

}
