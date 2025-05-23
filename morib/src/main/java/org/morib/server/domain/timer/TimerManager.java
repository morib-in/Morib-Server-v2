package org.morib.server.domain.timer;

import lombok.RequiredArgsConstructor;
import org.morib.server.annotation.Manager;
import org.morib.server.domain.timer.infra.Timer;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Manager
public class TimerManager {

    public int sumUserTotalElapsedTime(List<Timer> timers) {
        return timers.stream()
            .mapToInt(Timer::getElapsedTime)
            .sum();
    }

    @Transactional
    public void setElapsedTime(Timer timer, int elapsedTime) {
        timer.setElapsedTime(elapsedTime);
    }

    public void addElapsedTime(Timer timer, int elapsedTime){
        timer.addElapsedTime(elapsedTime);
    }

}
