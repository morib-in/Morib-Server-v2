package org.morib.server.domain.timer;

import lombok.RequiredArgsConstructor;
import org.morib.server.domain.timer.infra.TimerRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TimerReader {
    private final TimerRepository timerRepository;

    public void fetch() {
        // timerRepository에서 각 task의 timer 조회
    }
}
