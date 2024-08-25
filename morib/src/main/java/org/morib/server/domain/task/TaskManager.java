package org.morib.server.domain.task;

import lombok.RequiredArgsConstructor;
import org.morib.server.api.homeView.vo.TaskWithTimers;
import org.morib.server.domain.task.infra.Task;
import org.morib.server.domain.timer.infra.Timer;
import org.springframework.stereotype.Service;
import java.time.LocalDate;


@RequiredArgsConstructor
@Service
public class TaskManager {
    public TaskWithTimers classifyTimerByTask(LocalDate date, Task task) {
        int elapsedTime = task.getTimers().stream()
                .filter(timer -> timer.getTargetDate().equals(date))
                .map(Timer::getElapsedTime)
                .findFirst()
                .orElse(0); // 일치하는 타이머가 없으면 0으로 설정
        return new TaskWithTimers(date, task, elapsedTime);
    }
}
