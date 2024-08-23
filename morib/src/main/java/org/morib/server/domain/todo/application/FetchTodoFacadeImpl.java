package org.morib.server.api.timerView.service.fetch.todo;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.morib.server.annotation.Facade;
import org.morib.server.api.timerView.dto.TaskInTodoCardDto;
import org.morib.server.api.timerView.dto.TodoCardResponseDto;
import org.morib.server.api.timerView.service.fetch.task.FetchTaskService;
import org.morib.server.api.timerView.service.fetch.timer.FetchTimerService;
import org.morib.server.domain.task.infra.Task;
import org.morib.server.domain.todo.infra.Todo;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;

@Facade
@RequiredArgsConstructor
public class FetchTodoFacadeImpl implements FetchTodoFacade {

    private final FetchTodoService fetchTodoService;
    private final FetchTaskService fetchTaskService;
    private final FetchTimerService fetchTimerService;
    /**
     *
     * 타이머 뷰 안에서 todo 내의 task 들을 가져온다.
     * 해당 타이머들의 총 시간을 계산해야함!
     * 1. todo를 유저를 통해 찾는다.
     * 2. 찾은 todo 에서 task들을 불러온다.
     * 3. 찾은 task 들의 총 시간을 더하면 끝 -> task의 timer의 시간들을 다 더해야함!
     * @param targetDate
     * @return
     */
    @Override
    @Transactional
    public TodoCardResponseDto fetchTodoCard(LocalDate targetDate) {
        Long mockUserId = 1L;
        Todo todo = fetchTodoService.fetchByUserIdAndTargetDate(mockUserId, targetDate);
        LinkedHashSet<Task> tasks = fetchTaskService.fetchByTodoAndSameTargetDate(todo, targetDate);
        Long totalTimeOfToday = fetchTimerService.sumTasksElapsedTimeByTargetDate(tasks, targetDate);
        List<TaskInTodoCardDto> taskInTodoCardDtos = tasks.stream()
                .map(t -> getTaskInTodoCardDto(targetDate, t))
                .toList();

        return new TodoCardResponseDto(totalTimeOfToday, taskInTodoCardDtos);
    }

    private TaskInTodoCardDto getTaskInTodoCardDto(LocalDate targetDate, Task t) {
        return TaskInTodoCardDto.of(t, targetDate, fetchTimerService.sumOneTaskElapsedTimeInTargetDate(t, targetDate));
    }
}
