package org.morib.server.domain.task;

import lombok.RequiredArgsConstructor;
import org.morib.server.domain.task.infra.Task;
import org.morib.server.domain.timer.infra.Timer;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@RequiredArgsConstructor
@Service
public class TaskManager {
    public int classifyTimerByTask(LocalDate date, Task task) {
        return task.getTimers().stream()
                .filter(timer -> timer.getTargetDate().equals(date))
                .map(Timer::getElapsedTime)
                .findAny()
                .orElse(0); // 일치하는 Timer가 없으면 0을 반환
    }
}
