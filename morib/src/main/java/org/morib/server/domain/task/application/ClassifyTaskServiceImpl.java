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
        return (task.getStartDate().isBefore(date.plusDays(1))) &&
                (task.getEndDate() == null || task.getEndDate().isAfter(date.minusDays(1)));
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
