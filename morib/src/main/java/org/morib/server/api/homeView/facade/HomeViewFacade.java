package org.morib.server.api.homeView.facade;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.morib.server.annotation.Facade;
import org.morib.server.api.homeView.dto.StartTimerRequestDto;
import org.morib.server.api.homeView.dto.fetch.*;
import org.morib.server.api.homeView.vo.CategoriesByDate;
import org.morib.server.api.homeView.vo.CombinedByDate;
import org.morib.server.api.homeView.vo.TaskWithElapsedTime;
import org.morib.server.domain.category.application.ClassifyCategoryService;
import org.morib.server.domain.category.application.FetchCategoryService;
import org.morib.server.domain.task.application.ClassifyTaskService;
import org.morib.server.domain.task.application.FetchTaskService;
import org.morib.server.domain.task.application.ToggleTaskStatusService;
import org.morib.server.domain.task.infra.Task;
import org.morib.server.domain.timer.application.ClassifyTimerService;
import org.morib.server.domain.timer.application.FetchTimerService;
import org.morib.server.domain.timer.infra.Timer;
import org.morib.server.domain.todo.TodoManager;
import org.morib.server.domain.todo.application.CreateTodoService;
import org.morib.server.domain.todo.application.FetchTodoService;
import org.morib.server.domain.todo.infra.Todo;
import org.morib.server.domain.user.application.FetchUserService;
import org.morib.server.domain.user.infra.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Facade
public class HomeViewFacade {
    private final FetchCategoryService fetchCategoryService;
    private final ClassifyCategoryService classifyCategoryService;
    private final FetchTaskService fetchTaskService;
    private final ClassifyTaskService classifyTaskService;
    private final FetchTimerService fetchTimerService;
    private final ClassifyTimerService classifyTimerService;
    private final ToggleTaskStatusService toggleTaskStatusService;
    private final FetchUserService fetchUserService;
    private final FetchTodoService fetchTodoService;
    private final CreateTodoService createTodoService;
    private final TodoManager todoManager;

    public List<HomeViewResponseDto> fetchHome(HomeViewRequestDto request) {
        List<CategoriesByDate> categories = classifyCategoryService.classifyByDate(
                fetchCategoryService.fetchByUserIdInRange(request.userId(), request.startDate(), request.endDate()),
                request.startDate(), request.endDate());
        List<CombinedByDate> combined = classifyCategoryService.classifyTaskByCategory(categories);
        List<TaskWithElapsedTime> tasks = combined.stream()
                .flatMap(combinedByDate -> combinedByDate.getCombined().stream()
                .flatMap(tasksByCategory -> classifyTaskService.classifyTimerByTask(combinedByDate.getDate(), tasksByCategory.getTasks()).stream()
                )).toList();
        return convertToHomeViewResponseDto(createFetchCombinedDtoMap(combined, tasks));
    }

    private Map<LocalDate, List<FetchCombinedDto>> createFetchCombinedDtoMap(
            List<CombinedByDate> combined, List<TaskWithElapsedTime> tasks) {
        return combined.stream()
                .collect(Collectors.toMap(
                        CombinedByDate::getDate,
                        combinedByDate -> combinedByDate.getCombined().stream()
                                .map(entry -> {
                                    FetchCategoryDto categoryDto = FetchCategoryDto.of(entry.getCategory());
                                    List<FetchTaskDto> taskDtos = createFetchTaskDtoList(entry.getTasks(), tasks);
                                    return FetchCombinedDto.of(categoryDto, taskDtos);
                                })
                                .collect(Collectors.toList())
                ));
    }

    private List<FetchTaskDto> createFetchTaskDtoList(List<Task> tasks, List<TaskWithElapsedTime> taskWithElapsedTimes) {
        return tasks.stream()
                .map(task -> {
                    int elapsedTime = taskWithElapsedTimes.stream()
                            .filter(taskWithElapsedTime -> taskWithElapsedTime.getTask().equals(task))
                            .map(TaskWithElapsedTime::getElapsedTime)
                            .findFirst()
                            .orElse(0);
                    return FetchTaskDto.of(task, elapsedTime);
                })
                .collect(Collectors.toList());
    }

    private List<HomeViewResponseDto> convertToHomeViewResponseDto(Map<LocalDate, List<FetchCombinedDto>> combinedDtoMap) {
        return combinedDtoMap.entrySet().stream()
                .map(entry -> HomeViewResponseDto.of(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    public void fetchUserTimer() {
        fetchTaskService.fetch();
        fetchTimerService.fetch();
//        aggregateTimerService.aggregate();
    }

    public void createTask() {
//        createTaskService.create();
    }

    public void toggleTaskStatus() {
        fetchTaskService.fetch();
        toggleTaskStatusService.toggle();
    }

    @Transactional
    public void startTimer(Long mockUserId, StartTimerRequestDto startTimerRequestDto, LocalDate targetDate) {
        Todo todo = fetchTodoService.fetchByUserIdAndTargetDate(mockUserId, targetDate);
        Set<Task> tasks = fetchTaskService.fetchByTaskIds(startTimerRequestDto.taskIdList());
        todoManager.updateTask(todo, tasks);
    }

    @Transactional
    public FetchMyElapsedTimeResponseDto fetchMyElapsedTime(Long mockUserId, LocalDate targetDate) {
        User findUser = fetchUserService.fetchByUserId(mockUserId);
        Timer findTimer = fetchTimerService.fetchByUserAndTargetDate(findUser, targetDate);
        return FetchMyElapsedTimeResponseDto.of(findTimer);
    }
}
