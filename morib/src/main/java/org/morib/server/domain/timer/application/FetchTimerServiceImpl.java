package org.morib.server.domain.timer.application;

import java.time.LocalDate;
import java.util.Set;

import lombok.RequiredArgsConstructor;
import org.morib.server.domain.task.infra.Task;
import org.morib.server.domain.timer.infra.Timer;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FetchTimerServiceImpl implements FetchTimerService{

    private final TimerManager timerManager;

    @Override
    public void fetch() {

    }

    @Override
    public Timer fetchByTaskAndTargetDate(Task findTask, LocalDate targetDate) {
        return findTask.getTimers().stream()
                .filter(timer -> timer.getTargetDate().equals(targetDate))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("해당 timer가 없습니다."));
    }




    @Override
    public int sumTasksElapsedTimeByTargetDate(Set<Task> tasks, LocalDate targetDate) {
        return tasks.stream()
                .flatMap(t -> t.getTimers().stream())
                .filter(timer -> timer.getTargetDate().equals(targetDate))
                .mapToInt(Timer::getElapsedTime)
                .sum();
    }

    @Override
    public int sumOneTaskElapsedTimeInTargetDate(Task t, LocalDate targetDate) {
        return t.getTimers().stream()
                .filter(timer -> timer.getTargetDate().equals(targetDate))
                .mapToInt(Timer::getElapsedTime)
                .sum();
    }


}
