package org.morib.server.domain.timer;

import lombok.RequiredArgsConstructor;
import org.morib.server.domain.timer.infra.Timer;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TimerManager {

    public void aggregate() {
        // 각 task의 timer들을 종합해 오늘 나의 작업시간 계산
    }

    public void addElapsedTime(Timer timer, int elapsedTime){
        timer.addElapsedTime(elapsedTime);
    }

}
