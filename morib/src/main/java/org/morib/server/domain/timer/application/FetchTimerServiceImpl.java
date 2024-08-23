package org.morib.server.domain.timer.application;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.morib.server.domain.task.infra.Task;
import org.morib.server.domain.timer.infra.Timer;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FetchTimerServiceImpl implements FetchTimerService {

    private final TimeManager timeManager;

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
    public void addElapsedTime(Timer timer, int elapsedTime) {
        timeManager.addElapsedTime(timer, elapsedTime);
    }


}
