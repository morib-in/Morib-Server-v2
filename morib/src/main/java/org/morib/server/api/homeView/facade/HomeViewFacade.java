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
import org.morib.server.domain.category.infra.CategoryRepository;
import org.morib.server.domain.task.TaskManager;
import org.morib.server.domain.task.application.ClassifyTaskService;
import org.morib.server.domain.task.application.CreateTaskService;
import org.morib.server.domain.task.application.DeleteTaskService;
import org.morib.server.domain.task.application.FetchTaskService;
import org.morib.server.domain.task.infra.Task;
import org.morib.server.domain.timer.TimerManager;
import org.morib.server.domain.timer.application.CreateTimerService;
import org.morib.server.domain.timer.application.FetchTimerService;
import org.morib.server.domain.timer.infra.Timer;
import org.morib.server.domain.todo.TodoManager;
import org.morib.server.domain.todo.application.CreateTodoService;
import org.morib.server.domain.todo.application.FetchTodoService;
import org.morib.server.domain.todo.infra.Todo;
import org.morib.server.domain.user.application.service.FetchUserService;
import org.morib.server.domain.user.infra.User;
import org.morib.server.global.common.DataUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Facade
public class HomeViewFacade {

    private final FetchCategoryService fetchCategoryService;
    private final FetchTaskService fetchTaskService;
    private final DeleteTaskService deleteTaskService;
    private final FetchTimerService fetchTimerService;
    private final FetchUserService fetchUserService;
    private final FetchTodoService fetchTodoService;
    private final ClassifyTaskService classifyTaskService;
    private final CreateTaskService createTaskService;
    private final CreateTodoService createTodoService;
    private final CreateTimerService createTimerService;
    private final TodoManager todoManager;
    private final TimerManager timerManager;
    private final TaskManager taskManager;

    public List<HomeViewResponseDto> fetchHome(Long userId, LocalDate startDate, LocalDate endDate) {
        List<Category> findCategories = fetchCategoryService.fetchByUserIdWithFilteredTasksAndTimers(userId, startDate, endDate);
        return buildHomeViewResponseDto(startDate, endDate, findCategories);
    }

    private List<HomeViewResponseDto> buildHomeViewResponseDto(LocalDate startDate, LocalDate endDate, List<Category> findCategories) {
        List<HomeViewResponseDto> homeViewList = new ArrayList<>();

        // 날짜 범위별로 데이터를 분류
        for (LocalDate idxDate = startDate; !idxDate.isAfter(endDate); idxDate = idxDate.plusDays(1)) {
            LocalDate currentIdxDate = idxDate;
            List<CombinedCategoryAndTaskInfo> combinedCategoryAndTaskInfos = findCategories.stream()
                    .map(category -> {
                        // 특정 날짜(idxDate)에 해당하는 Task 필터링
                        List<TaskInfo> taskInfos = category.getTasks().stream()
                                .filter(task -> classifyTaskService.isTaskInDateRange(task, currentIdxDate))
                                .map(task -> TaskInfo.of(task, fetchTimerService.fetchElapsedTimeOrZeroByTaskAndTargetDate(task, currentIdxDate)))
                                .toList();

                        // Task가 없어도 Category는 표시해야 하므로 빈 리스트라도 포함
                        return CombinedCategoryAndTaskInfo.of(CategoryInfo.of(category), !taskInfos.isEmpty() ? taskInfos : Collections.emptyList());
                    }).toList();
            homeViewList.add(HomeViewResponseDto.of(idxDate, combinedCategoryAndTaskInfos));
        }
        return homeViewList;
    }

    @Transactional
    public void createTask(Long mockUserId, Long categoryId,
                           CreateTaskRequestDto requestDto) {
        User findUser = fetchUserService.fetchByUserId(mockUserId);
        Category findCategory = fetchCategoryService.fetchByUserAndCategoryId(findUser, categoryId);
        createTaskService.createTaskByCategoryAndBetweenDate(findCategory, requestDto.name(),
                requestDto.startDate(), requestDto.endDate());
    }

    @Transactional
    public void toggleTaskStatus(Long taskId) {
        Task findTask = fetchTaskService.fetchById(taskId);
        taskManager.toggleTaskStatus(findTask);
    }

    @Transactional
    public void startTimer(Long userId, StartTimerRequestDto startTimerRequestDto, LocalDate targetDate) {
        // 사용자 조회 (한 번만)
        User findUser = fetchUserService.fetchByUserId(userId);

        // Todo 조회 및 생성 (필요한 경우에만)
        Todo todo = fetchTodoService.fetchOrNullByUserIdAndTargetDate(userId, targetDate)
                .orElseGet(() -> createTodoService.saveTodoByUserAndTargetDate(findUser, targetDate));

        // Task ID 리스트 조회 (Batch 조회)
        List<Task> tasks = fetchTaskService.fetchByTaskIds(startTimerRequestDto.taskIdList());

        // 타이머가 존재하는 Task 조회 (Batch 조회)
        Set<Long> existingTimerTaskIds = fetchTimerService.fetchExistingTaskIdsByTargetDate(tasks, targetDate);

        // Task 업데이트 & 타이머 생성 (존재하지 않는 타이머만)
        updateTaskInTodo(startTimerRequestDto, todo);
        tasks.stream()
                .filter(task -> !existingTimerTaskIds.contains(task.getId())) // 이미 존재하는 타이머는 제외
                .forEach(task -> createTimerService.createTimer(findUser, targetDate, task));
    }

    private void updateTaskInTodo(StartTimerRequestDto startTimerRequestDto, Todo todo) {
        List<Task> tasks = fetchTaskService.fetchByTaskIds(startTimerRequestDto.taskIdList());
        todoManager.updateTask(todo, tasks);
    }


    @Transactional
    public FetchMyElapsedTimeResponseDto fetchTotalElapsedTimeTodayByUser(Long userId, LocalDate targetDate) {
        User findUser = fetchUserService.fetchByUserId(userId);
        List<Timer> findTodayTimer = fetchTimerService.fetchByUserAndTargetDate(findUser, targetDate);
        int sumUserTotalElapsedTime = timerManager.sumUserTotalElapsedTime(findTodayTimer);
        return FetchMyElapsedTimeResponseDto.of(targetDate, sumUserTotalElapsedTime);
    }

    @Transactional
    public void deleteTask(Long taskId) {
        deleteTaskService.deleteByTaskId(taskId);
    }

    // deprecated Home Fetch
    // 비교 테스트를 위해 둠
    public List<HomeViewResponseDto> fetchHomeDeprecated(HomeViewRequestDto request) {
        // 1. 사용자의 모든 카테고리 가져오기
        User findUser = fetchUserService.fetchByUserId(request.userId());
        List<Category> categories = fetchCategoryService.fetchByUser(findUser).stream().toList();

        // 2. 카테고리에 태스크+타이머(TaskWithTimers) 붙이기
        LinkedHashSet<CategoryWithTasks> categoryWithTasks = fetchTasksByCategories(categories);
        return classifyAndBuildHomeViewResponseDto(request.startDate(), request.endDate(), categoryWithTasks);
    }

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

}
