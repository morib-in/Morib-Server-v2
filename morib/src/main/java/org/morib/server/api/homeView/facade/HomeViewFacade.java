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
import org.morib.server.domain.user.application.service.FetchUserService;
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
    private final FetchTimerService fetchTimerService;
    private final FetchUserService fetchUserService;
    private final FetchTodoService fetchTodoService;
    private final ClassifyTaskService classifyTaskService;
    private final CreateTaskService createTaskService;
    private final CreateTodoService createTodoService;
    private final TodoManager todoManager;
    private final TimerManager timerManager;
    private final TaskManager taskManager;

    public List<HomeViewResponseDto> fetchHome(HomeViewRequestDto request) {
        // 1. 사용자의 모든 카테고리 가져오기
        User findUser = fetchUserService.fetchByUserId(request.userId());
        List<Category> categories = fetchCategoryService.fetchByUser(findUser).stream().toList();

        // 2. 카테고리에 태스크+타이머(TaskWithTimers) 붙이기
        LinkedHashSet<CategoryWithTasks> categoryWithTasks = fetchTasksByCategories(categories);
        return classifyAndBuildHomeViewResponseDto(request.startDate(), request.endDate(), categoryWithTasks);
    }

    /*
    Category + TaskWithTimers(Task + Set<Timer>)
     */
    // 카테고리로 태스크 조회
    private LinkedHashSet<CategoryWithTasks> fetchTasksByCategories(List<Category> categories) {
        return categories.stream()
                .map(this::convertToCategoryWithTasks)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    // TaskWithTimers -> CategoryWithTasks
    private CategoryWithTasks convertToCategoryWithTasks(Category category) {
        LinkedHashSet<TaskWithTimers> taskWithTimers = convertToTaskWithTimers(classifyTaskService.sortTasksByCreatedAt(category.getTasks()));
        return fetchCategoryService.convertToCategoryWithTasks(category, taskWithTimers);
    }

    // Task -> TaskWithTimers
    private LinkedHashSet<TaskWithTimers> convertToTaskWithTimers(LinkedHashSet<Task> tasks) {
        return tasks.stream()
                .map(fetchTaskService::convertToTaskWithTimers)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    // 날짜별로 분류해서 최종 Response 만들기
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

    // 날짜별로 dto 분리
    private List<CombinedCategoryAndTaskInfo> buildFetchCombinedDtoByDate(LocalDate idxDate, LinkedHashSet<CategoryWithTasks> categoryWithTasks) {
        return categoryWithTasks.stream()
                .map(ct -> buildFetchCombinedDto(ct, idxDate))
                .toList();
    }

    // combinedDto 생성
    private CombinedCategoryAndTaskInfo buildFetchCombinedDto(CategoryWithTasks categoryWithTasks, LocalDate idxDate) {
        List<TaskInfo> taskInfos = categoryWithTasks.taskWithTimers().stream()
                .filter(twt -> DataUtils.isInRange(idxDate, twt.task().getStartDate(), twt.task().getEndDate())) // Task의 기간만 필터링
                .map(twt -> TaskInfo.of(twt.task(), fetchTimerService.fetchElapsedTimeOrZeroByTaskAndTargetDate(twt.task(), idxDate)))
                .toList();
        if (!taskInfos.isEmpty()) {
            return CombinedCategoryAndTaskInfo.of(CategoryInfo.of(categoryWithTasks.category()), taskInfos);
        }
        return CombinedCategoryAndTaskInfo.of(CategoryInfo.of(categoryWithTasks.category()), Collections.emptyList());
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
        Optional<Todo> optionalTodo = fetchTodoService.fetchOrNullByUserIdAndTargetDate(mockUserId, targetDate);
        optionalTodo.ifPresentOrElse(todo -> updateTaskInTodo(startTimerRequestDto, todo), () -> {
            User findUser = fetchUserService.fetchByUserId(mockUserId);
            Todo newTodo = createTodoService.saveTodoByUserAndTargetDate(findUser, targetDate);
            updateTaskInTodo(startTimerRequestDto, newTodo);
        });
    }

    private void updateTaskInTodo(StartTimerRequestDto startTimerRequestDto, Todo todo) {
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
