package org.morib.server.api.homeView.facade;

import org.morib.server.api.homeView.dto.fetch.HomeViewResponseDto;
import org.morib.server.api.homeView.vo.CategoryWithTasks;
import org.morib.server.domain.task.infra.Task;
import org.morib.server.domain.timer.infra.Timer;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;

@Component
public class HomeDtoBuilder {
    public List<HomeViewResponseDto> filterAndConvertToDto(LinkedHashSet<CategoryWithTasks> categoryWithTasksSet, LocalDate idxDate) {
        return null;
    }


    private boolean isInRange(LocalDate idxDate, LocalDate startDate, LocalDate endDate) {
        return !idxDate.isBefore(startDate) && !idxDate.isAfter(endDate);
    }

    private int fetchTimerByDate(LocalDate idxDate, Task task) {
        return task.getTimers().stream()
                .filter(timer -> timer.getTargetDate().equals(idxDate))
                .findFirst().map(Timer::getElapsedTime)
                .orElse(0);
    }
}
