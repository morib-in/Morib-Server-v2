package org.morib.server.domain.timer;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.morib.server.annotation.Manager;
import org.morib.server.domain.timer.infra.Timer;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Manager
public class TimerManager {

    public int sumUserTotalElapsedTime(List<Timer> timers) {
        return timers.stream()
            .mapToInt(Timer::getElapsedTime)
            .sum();
    }

    public void addElapsedTime(Timer timer, int elapsedTime){
        timer.addElapsedTime(elapsedTime);
    }

}
