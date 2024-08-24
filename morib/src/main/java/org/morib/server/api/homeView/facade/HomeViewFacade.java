package org.morib.server.api.homeView.facade;

import lombok.RequiredArgsConstructor;
import org.morib.server.api.homeView.dto.fetch.*;
import org.morib.server.api.homeView.vo.CategoriesByDate;
import org.morib.server.api.homeView.vo.CombinedByDate;
import org.morib.server.api.homeView.vo.TaskWithElapsedTime;
import org.morib.server.domain.category.application.ClassifyCategoryService;
import org.morib.server.domain.category.application.FetchCategoryService;
import org.morib.server.domain.task.application.ClassifyTaskService;
import org.morib.server.domain.task.application.FetchTaskService;
import org.morib.server.domain.task.application.ToggleTaskStatusService;
import org.morib.server.domain.timer.application.ClassifyTimerService;
import org.morib.server.domain.timer.application.FetchTimerService;
import org.springframework.stereotype.Service;

import java.util.List;

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
        List<CategoriesByDate> categories = classifyCategoryService.classifyByDate(
                fetchCategoryService.fetchByUserIdInRange(request.userId(), request.startDate(), request.endDate()),
                request.startDate(), request.endDate());
        List<CombinedByDate> combined = classifyCategoryService.classifyTaskByCategory(categories);
        List<TaskWithElapsedTime> tasks = combined.stream()
                .flatMap(combinedByDate -> combinedByDate.getCombined().stream()
                .flatMap(tasksByCategory -> classifyTaskService.classifyTimerByTask(combinedByDate.getDate(), tasksByCategory.getTasks()).stream()
                )).toList();
        return homeDtoBuilder.convertToHomeViewResponseDto(homeDtoBuilder.createFetchCombinedDtoMap(combined, tasks));
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
