package org.morib.server.domain.timer.application;

import lombok.RequiredArgsConstructor;
import org.morib.server.domain.task.infra.Task;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@RequiredArgsConstructor
@Service
public class ClassifyTimerServiceImpl implements ClassifyTimerService{
    private final TimerManager timerManager;

    @Override
    public int classifyTaskByDate(LocalDate date, Task task) {
        return 0;
    }
}
