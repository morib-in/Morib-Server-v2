package org.morib.server.domain.timer.application;

import java.time.LocalDate;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.morib.server.domain.task.infra.Task;
import org.morib.server.domain.timer.infra.Timer;
import org.morib.server.domain.timer.infra.TimerRepository;
import org.morib.server.domain.user.infra.User;
import org.morib.server.global.exception.NotFoundException;
import org.morib.server.global.message.ErrorMessage;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FetchTimerServiceImpl implements FetchTimerService{

    private final TimerRepository timerRepository;

    @Override
    public Timer fetchByTaskAndTargetDate(Task findTask, LocalDate targetDate) {
        return findTask.getTimers().stream()
                .filter(timer -> timer.getTargetDate().equals(targetDate))
                .findFirst().orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND));
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

}
