package org.morib.server.api.timerView.service.fetch.timer;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.morib.server.domain.task.infra.Task;
import org.morib.server.domain.timer.TimerOperator;
import org.morib.server.domain.timer.infra.Timer;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FetchTimerServiceImpl implements FetchTimerService {

    private final TimerOperator timerOperator;

    @Override
    public void fetch() {

    }

    @Override
    public Timer fetchByTaskAndTargetDate(Task findTask, LocalDate localDate) {
        return findTask.getTimers().stream()
            .filter(timer -> timer.getTargetDate().equals(localDate))
            .findFirst().orElseThrow(() -> new IllegalArgumentException("해당 timer가 없습니다."));
    }

    @Override
    public void addElapsedTime(Timer timer, int elapsedTime) {
        timerOperator.addElapsedTime(timer, elapsedTime);
    }


}
