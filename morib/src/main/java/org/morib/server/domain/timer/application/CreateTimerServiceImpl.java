package org.morib.server.domain.timer.application;

import lombok.RequiredArgsConstructor;
import org.morib.server.domain.task.infra.Task;
import org.morib.server.domain.timer.infra.Timer;
import org.morib.server.domain.timer.infra.TimerRepository;
import org.morib.server.domain.user.infra.User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class CreateTimerServiceImpl implements CreateTimerService{

    private final TimerRepository timerRepository;

    @Override
    public void createTimer(User user, LocalDate targetDate, Task task) {
        timerRepository.save(Timer.create(user, targetDate, task));
    }
}
