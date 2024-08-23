package org.morib.server.api.homeViewApi.service;

import lombok.RequiredArgsConstructor;
import org.morib.server.api.homeViewApi.dto.fetch.*;
import org.morib.server.api.homeViewApi.service.aggregate.timer.AggregateTimerService;
import org.morib.server.api.homeViewApi.service.classify.category.ClassifyCategoryService;
import org.morib.server.api.homeViewApi.service.classify.task.ClassifyTaskService;
import org.morib.server.api.homeViewApi.service.classify.timer.ClassifyTimerService;
import org.morib.server.api.homeViewApi.service.create.task.CreateTaskService;
import org.morib.server.api.homeViewApi.service.create.timer.CreateTimerService;
import org.morib.server.api.homeViewApi.service.create.todo.CreateTodoService;
import org.morib.server.api.homeViewApi.service.fetch.category.FetchCategoryService;
import org.morib.server.api.homeViewApi.service.fetch.task.FetchTaskService;
import org.morib.server.api.homeViewApi.service.fetch.timer.FetchTimerService;
import org.morib.server.api.homeViewApi.service.toggle.task.ToggleTaskStatusService;
import org.morib.server.domain.category.infra.Category;
import org.morib.server.domain.task.infra.Task;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
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
    private final AggregateTimerService aggregateTimerService;
    private final CreateTaskService createTaskService;
    private final ToggleTaskStatusService toggleTaskStatusService;
    private final CreateTimerService createTimerService;
    private final CreateTodoService createTodoService;

    public List<HomeViewResponseDto> fetchHome(HomeViewRequestDto request) {
        Map<LocalDate, List<Category>> categories = classifyCategoryService.classifyByDate(fetchCategoryService.fetch(request), request.startDate(), request.endDate());
        Map<LocalDate, Map<Category, List<Task>>> combined = classifyCategoryService.classifyTaskByCategory(categories);
        Map<LocalDate, List<FetchCombinedDto>> combinedDto = combined.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().entrySet().stream()
                                .map(categoryEntry -> {
                                    FetchCategoryDto categoryDto = FetchCategoryDto.of(categoryEntry.getKey());
                                    List<FetchTaskDto> taskDtos = categoryEntry.getValue().stream()
                                            .map(task -> {
                                                int elapsedTime = classifyTaskService.classifyTimerByTask(entry.getKey(), task);
                                                return FetchTaskDto.of(task, elapsedTime);
                                            })
                                            .collect(Collectors.toList());
                                    return FetchCombinedDto.of(categoryDto, taskDtos);
                                })
                                .toList()
                ));
        return combinedDto.entrySet().stream()
                .map(entry -> HomeViewResponseDto.of(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    public void fetchUserTimer() {
        fetchTaskService.fetch();
        fetchTimerService.fetch();
        aggregateTimerService.aggregate();
    }

    public void createTask() {
        createTaskService.create();
    }

    public void toggleTaskStatus() {
        fetchTaskService.fetch();
        toggleTaskStatusService.toggle();
    }

    public void startTimer() {
        createTodoService.create();
        createTimerService.create();
    }
}
