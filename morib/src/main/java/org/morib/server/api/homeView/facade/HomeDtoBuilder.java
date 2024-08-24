package org.morib.server.api.homeView.facade;

import org.morib.server.api.homeView.dto.fetch.FetchCategoryDto;
import org.morib.server.api.homeView.dto.fetch.FetchCombinedDto;
import org.morib.server.api.homeView.dto.fetch.FetchTaskDto;
import org.morib.server.api.homeView.dto.fetch.HomeViewResponseDto;
import org.morib.server.api.homeView.vo.CombinedByDate;
import org.morib.server.api.homeView.vo.TaskWithElapsedTime;
import org.morib.server.domain.task.infra.Task;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class HomeDtoBuilder {
    protected Map<LocalDate, List<FetchCombinedDto>> createFetchCombinedDtoMap(
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

    protected List<FetchTaskDto> createFetchTaskDtoList(List<Task> tasks, List<TaskWithElapsedTime> taskWithElapsedTimes) {
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

    protected List<HomeViewResponseDto> convertToHomeViewResponseDto(Map<LocalDate, List<FetchCombinedDto>> combinedDtoMap) {
        return combinedDtoMap.entrySet().stream()
                .map(entry -> HomeViewResponseDto.of(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }
}
