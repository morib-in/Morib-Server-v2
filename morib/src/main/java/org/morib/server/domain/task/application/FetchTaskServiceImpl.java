package org.morib.server.domain.task.application;

import lombok.RequiredArgsConstructor;
import org.morib.server.api.homeView.vo.TaskWithTimers;
import org.morib.server.domain.task.infra.Task;
import org.morib.server.domain.task.infra.TaskRepository;
import org.morib.server.domain.timer.infra.Timer;
import org.morib.server.domain.todo.infra.Todo;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FetchTaskServiceImpl implements FetchTaskService {

    private final TaskRepository taskRepository;

    @Override
    public void fetch() {
        //timer가 멈췄을때 계산해주는 역할을 해야함!

    }

    @Override
    public Task fetchById(Long taskId) {
        return taskRepository.findById(taskId).orElseThrow(() -> new IllegalArgumentException("해당 task가 없습니다."));
    }

    @Override
    public LinkedHashSet<Task> fetchByTodoAndSameTargetDate(Todo todo, LocalDate targetDate) {
        Set<Task> tasks = todo.getTasks();
        return tasks.stream().
            filter(task -> isTimerInTaskPresentTargetDate(task, targetDate))
            .collect(Collectors.toCollection(LinkedHashSet::new));
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
