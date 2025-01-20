package org.morib.server.domain.task.application;

import lombok.RequiredArgsConstructor;
import org.morib.server.api.homeView.vo.TaskWithTimers;
import java.time.LocalDate;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.morib.server.domain.category.infra.Category;
import org.morib.server.domain.task.infra.Task;
import org.morib.server.domain.task.infra.TaskRepository;
import org.morib.server.domain.timer.infra.Timer;
import org.morib.server.domain.todo.infra.Todo;
import org.morib.server.global.exception.BusinessException;
import org.morib.server.global.exception.NotFoundException;
import org.morib.server.global.message.ErrorMessage;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FetchTaskServiceImpl implements FetchTaskService {

    private final TaskRepository taskRepository;

    @Override
    public Task fetchById(Long taskId) {
        return taskRepository.findById(taskId).orElseThrow(() ->
            new NotFoundException(ErrorMessage.NOT_FOUND));
    }

    @Override
    public LinkedHashSet<Task> fetchByTodoAndSameTargetDate(Todo todo, LocalDate targetDate) {
        Set<Task> tasks = todo.getTasks();
        return tasks.stream().
            filter(task -> isTimerInTaskPresentTargetDate(task, targetDate))
            .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public Set<Task> fetchByTaskIds(List<Long> taskIds) {
        Set<Task> tasks = new LinkedHashSet<>();
        for (Long taskId : taskIds) {
            Task findTask = taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND));
            tasks.add(findTask);
        }
        return convertUnmmodifiableSet(tasks);
    }

    private Set<Task> convertUnmmodifiableSet(Set<Task> tasks) {
        return Collections.unmodifiableSet(tasks);
    }

    private boolean isTimerInTaskPresentTargetDate(Task task, LocalDate targetDate) {
        Set<Timer> timers = task.getTimers();
        return timers.stream().anyMatch(t -> t.getTargetDate().equals(targetDate));
    }

    public TaskWithTimers convertToTaskWithTimers(Task task) {
        Set<Timer> timers = task.getTimers();
        return TaskWithTimers.of(task, timers);
    }




}
