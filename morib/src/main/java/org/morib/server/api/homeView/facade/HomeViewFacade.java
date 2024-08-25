package org.morib.server.api.homeView.facade;

import lombok.RequiredArgsConstructor;
import org.morib.server.api.homeView.dto.fetch.HomeViewRequestDto;
import org.morib.server.api.homeView.dto.fetch.HomeViewResponseDto;
import org.morib.server.api.homeView.vo.CategoryWithTasks;
import org.morib.server.api.homeView.vo.TaskWithTimers;
import org.morib.server.domain.category.application.ClassifyCategoryService;
import org.morib.server.domain.category.application.FetchCategoryService;
import org.morib.server.domain.category.infra.Category;
import org.morib.server.domain.task.application.ClassifyTaskService;
import org.morib.server.domain.task.application.FetchTaskService;
import org.morib.server.domain.task.application.ToggleTaskStatusService;
import org.morib.server.domain.task.infra.Task;
import org.morib.server.domain.timer.application.ClassifyTimerService;
import org.morib.server.domain.timer.application.FetchTimerService;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class HomeViewFacade {
    private final FetchCategoryService fetchCategoryService;
    private final ClassifyCategoryService classifyCategoryService;
    private final FetchTaskService fetchTaskService;
    private final ClassifyTaskService classifyTaskService;
    private final FetchTimerService fetchTimerService;
    private final ClassifyTimerService classifyTimerService;
    private final ToggleTaskStatusService toggleTaskStatusService;
    private final HomeDtoBuilder homeDtoBuilder;

    public List<HomeViewResponseDto> fetchHome(HomeViewRequestDto request) {
        List<Category> categories = fetchCategoryService.fetchByUserIdInRange(request.userId(), request.startDate(), request.endDate());
        LinkedHashSet<CategoryWithTasks> categoryWithTasks = fetchTasksByCategories(categories);
        // Dto Build
        return null;
    }

    private LinkedHashSet<CategoryWithTasks> fetchTasksByCategories(List<Category> categories) {
        return categories.stream()
                .map(this::convertToCategoryWithTasks)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    // Category -> CategoryWithTasks
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

    public void startTimer() {
//        createTodoService.create();
//        createTimerService.create();
    }
}
