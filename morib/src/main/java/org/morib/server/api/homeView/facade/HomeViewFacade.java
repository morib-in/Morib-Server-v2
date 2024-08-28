package org.morib.server.api.homeView.facade;

import lombok.RequiredArgsConstructor;
import org.morib.server.annotation.Facade;
import org.morib.server.api.homeView.dto.CreateTaskRequestDto;
import org.morib.server.api.homeView.dto.StartTimerRequestDto;
import org.morib.server.api.homeView.dto.fetch.FetchMyElapsedTimeResponseDto;
import org.morib.server.api.homeView.dto.fetch.HomeViewRequestDto;
import org.morib.server.api.homeView.dto.fetch.HomeViewResponseDto;
import org.morib.server.api.homeView.vo.*;
import org.morib.server.domain.category.application.FetchCategoryService;
import org.morib.server.domain.category.infra.Category;
import org.morib.server.domain.task.TaskManager;
import org.morib.server.domain.task.application.ClassifyTaskService;
import org.morib.server.domain.task.application.CreateTaskService;
import org.morib.server.domain.task.application.FetchTaskService;
import org.morib.server.domain.task.infra.Task;
import org.morib.server.domain.timer.TimerManager;
import org.morib.server.domain.timer.application.FetchTimerService;
import org.morib.server.domain.timer.infra.Timer;
import org.morib.server.domain.todo.TodoManager;
import org.morib.server.domain.todo.application.CreateTodoService;
import org.morib.server.domain.todo.application.FetchTodoService;
import org.morib.server.domain.todo.infra.Todo;
import org.morib.server.domain.user.application.FetchUserService;
import org.morib.server.domain.user.infra.User;
import org.morib.server.global.common.DataUtils;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Facade
public class HomeViewFacade {
    private final FetchCategoryService fetchCategoryService;
    private final FetchTaskService fetchTaskService;
    private final ClassifyTaskService classifyTaskService;
    private final FetchTimerService fetchTimerService;
    private final CreateTaskService createTaskService;
    private final FetchUserService fetchUserService;
    private final FetchTodoService fetchTodoService;
    private final CreateTodoService createTodoService;
    private final TodoManager todoManager;
    private final TimerManager timerManager;
    private final TaskManager taskManager;

    public List<HomeViewResponseDto> fetchHome(HomeViewRequestDto request) {
        List<Category> categories = fetchCategoryService.fetchByUserIdInRange(request.userId(), request.startDate(), request.endDate());
        LinkedHashSet<CategoryWithTasks> categoryWithTasks = fetchTasksByCategories(categories);
        return classifyAndBuildHomeViewResponseDto(request.startDate(), request.endDate(), categoryWithTasks);
    }

    private LinkedHashSet<CategoryWithTasks> fetchTasksByCategories(List<Category> categories) {
        return categories.stream()
                .map(this::convertToCategoryWithTasks)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private CategoryWithTasks convertToCategoryWithTasks(Category category) {
        LinkedHashSet<TaskWithTimers> taskWithTimers = convertToTaskWithTimers(classifyTaskService.sortTasksByCreatedAt(category.getTasks()));
        return fetchCategoryService.convertToCategoryWithTasks(category, taskWithTimers);
    }

    private LinkedHashSet<TaskWithTimers> convertToTaskWithTimers(LinkedHashSet<Task> tasks) {
        return tasks.stream()
                .map(fetchTaskService::convertToTaskWithTimers)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private List<HomeViewResponseDto> classifyAndBuildHomeViewResponseDto(LocalDate startDate, LocalDate endDate, LinkedHashSet<CategoryWithTasks> categoryWithTasks) {
        List<HomeViewResponseDto> result = new ArrayList<>();
        for (LocalDate idxDate = startDate; !idxDate.isAfter(endDate); idxDate = idxDate.plusDays(1)) {
            List<CombinedCategoryAndTaskInfo> combinedCategoryAndTaskInfos = buildFetchCombinedDtoByDate(idxDate, categoryWithTasks);

            if (!combinedCategoryAndTaskInfos.isEmpty()) {
                result.add(HomeViewResponseDto.of(idxDate, combinedCategoryAndTaskInfos));
            }
        }
        return result;
    }

    private List<CombinedCategoryAndTaskInfo> buildFetchCombinedDtoByDate(LocalDate idxDate, LinkedHashSet<CategoryWithTasks> categoryWithTasks) {
        return categoryWithTasks.stream()
                .filter(ct -> DataUtils.isInRange(idxDate, ct.category().getStartDate(), ct.category().getEndDate()))
                .map(ct -> buildFetchCombinedDto(ct, idxDate))
                .filter(Objects::nonNull)
                .toList();
    }

    private CombinedCategoryAndTaskInfo buildFetchCombinedDto(CategoryWithTasks categoryWithTasks, LocalDate idxDate) {
        List<TaskInfo> taskInfos = categoryWithTasks.taskWithTimers().stream()
                .filter(twt -> DataUtils.isInRange(idxDate, twt.task().getStartDate(), twt.task().getEndDate()))
                .map(twt -> TaskInfo.of(twt.task(), fetchTimerService.fetchElapsedTimeOrZeroByTaskAndTargetDate(twt.task(), idxDate)))
                .toList();
        if (!taskInfos.isEmpty()) {
            return CombinedCategoryAndTaskInfo.of(CategoryInfo.of(categoryWithTasks.category()), taskInfos);
        }
        return null;
    }

    @Transactional
    public void createTask(Long mockUserId, Long categoryId,
        CreateTaskRequestDto requestDto) {
        User findUser = fetchUserService.fetchByUserId(mockUserId);
        Category findCategory = fetchCategoryService.fetchByUserAndCategoryId(findUser,categoryId);
        createTaskService.createTaskByCategoryAndBetweenDate(findCategory, requestDto.name(),
            requestDto.startDate(), requestDto.endDate());
    }

    @Transactional
    public void toggleTaskStatus(Long taskId) {
        Task findTask = fetchTaskService.fetchById(taskId);
        taskManager.toggleTaskStatus(findTask);
    }

    @Transactional
    public void startTimer(Long mockUserId, StartTimerRequestDto startTimerRequestDto, LocalDate targetDate) {
        Todo todo = fetchTodoService.fetchByUserIdAndTargetDate(mockUserId, targetDate);
        Set<Task> tasks = fetchTaskService.fetchByTaskIds(startTimerRequestDto.taskIdList());
        todoManager.updateTask(todo, tasks);
    }

    @Transactional
    public FetchMyElapsedTimeResponseDto fetchTotalElapsedTimeTodayByUser(Long mockUserId, LocalDate targetDate) {
        User findUser = fetchUserService.fetchByUserId(mockUserId);
        List<Timer> findTodayTimer = fetchTimerService.fetchByUserAndTargetDate(findUser, targetDate);
        int sumUserTotalElapsedTime = timerManager.sumUserTotalElapsedTime(findTodayTimer);
        return FetchMyElapsedTimeResponseDto.of(targetDate, sumUserTotalElapsedTime);
    }
}
