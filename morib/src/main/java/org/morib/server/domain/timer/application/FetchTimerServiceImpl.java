package org.morib.server.domain.timer.application;

import lombok.RequiredArgsConstructor;
import org.morib.server.domain.task.infra.Task;
import org.morib.server.domain.timer.infra.Timer;
import org.morib.server.domain.timer.infra.TimerRepository;
import org.morib.server.domain.user.infra.User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class FetchTimerServiceImpl implements FetchTimerService{

    private final TimerRepository timerRepository;

    @Override
    public Timer fetchByTaskAndTargetDate(Task findTask, LocalDate targetDate) {
        return findTask.getTimers().stream()
                .filter(timer -> timer.getTargetDate().isEqual(targetDate))
                .findFirst()
                .orElse(null); // Timer가 없으면 null 반환
    }

    @Override
    public int fetchElapsedTimeOrZeroByTaskAndTargetDate(Task findTask, LocalDate targetDate) {
        return findTask.getTimers().stream()
                .filter(timer -> timer.getTargetDate().equals(targetDate))
                .findFirst()
                .map(Timer::getElapsedTime)
                .orElse(0);
    }

    @Override
    public int sumOneTaskElapsedTimeInTargetDate(Task t, LocalDate targetDate) {
        return t.getTimers().stream()
                .filter(timer -> timer.getTargetDate().equals(targetDate))
                .mapToInt(Timer::getElapsedTime)
                .sum();
    }

    @Override
    public int sumElapsedTimeByUser(User user, LocalDate targetDate) {
        return timerRepository.findByUser(user).stream()
            .filter(timer -> timer.getTargetDate().equals(targetDate))
            .mapToInt(Timer::getElapsedTime)
            .sum();
    }

    @Override
    public List<Timer> fetchByUserAndTargetDate(User user, LocalDate targetDate) {
        return timerRepository.findByUserAndTargetDate(user, targetDate);
    }

    @Override
    public Set<Long> fetchExistingTaskIdsByTargetDate(List<Task> tasks, LocalDate targetDate) {
        List<Long> taskIds = tasks.stream()
                .map(Task::getId)
                .toList(); // Task ID 리스트 변환
        return timerRepository.findExistingTaskIdsByTargetDate(taskIds, targetDate);
    }

    @Override
    public Timer fetchByTaskIdAndTargetDate(Long taskId, LocalDate targetDate) {
        return timerRepository.findByTaskIdAndTargetDate(taskId, targetDate);
    }

    @Override
    public Timer fetchOrCreateByTaskAndTargetDate(User user, Task task, LocalDate targetDate) {
        Timer timer = fetchByTaskAndTargetDate(task, targetDate);
        if (timer == null) {
            timer = Timer.create(user, targetDate, task);
        }
        return timer;
    }

}
