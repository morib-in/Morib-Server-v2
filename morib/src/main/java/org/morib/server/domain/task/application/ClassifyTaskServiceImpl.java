package org.morib.server.domain.task.application;

import lombok.RequiredArgsConstructor;
import org.morib.server.domain.task.infra.Task;
import org.morib.server.global.exception.InvalidTaskInTodoException;
import org.morib.server.global.message.ErrorMessage;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ClassifyTaskServiceImpl implements ClassifyTaskService {

    @Override
    public LinkedHashSet<Task> sortTasksByCreatedAt(Set<Task> tasks) {
        return tasks.stream()
                .sorted(Comparator.comparing(Task::getCreatedAt)) // createdAt 필드를 기준으로 정렬
                .collect(Collectors.toCollection(LinkedHashSet::new)); // LinkedHashSet으로 수집
    }

    @Override
    public boolean isTaskInDateRange(Task task, LocalDate date) {
        LocalDate startDate = task.getStartDate();
        LocalDate endDate = task.getEndDate();

        if (endDate == null) {
            // endDate가 null이면, date가 startDate와 정확히 같은 날짜여야 합니다.
            return date.equals(startDate);
        } else {
            // endDate가 null이 아니면, date가 startDate와 endDate 사이에 있거나 경계와 같아야 합니다.
            // 즉, startDate <= date <= endDate
            // !date.isBefore(startDate)는 date >= startDate 와 동일합니다.
            // !date.isAfter(endDate)는 date <= endDate 와 동일합니다.
            return !date.isBefore(startDate) && !date.isAfter(endDate);
        }
    }

    @Override
    public void validateIncludingCompletedTasks(List<Task> tasks) {
        if (tasks.stream().anyMatch(Task::getIsComplete)) {
            throw new InvalidTaskInTodoException(ErrorMessage.INVALID_TASK_IN_TODO);
        }
    }

    @Override
    public void validateIncludingCompletedTask(Task task) {
        if (task.getIsComplete()) {
            throw new InvalidTaskInTodoException(ErrorMessage.INVALID_TASK_IN_TODO);
        }
    }
}
