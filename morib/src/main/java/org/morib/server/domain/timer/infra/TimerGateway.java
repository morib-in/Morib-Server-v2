package org.morib.server.domain.timer.infra;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TimerGateway {
    private final TimerRepository timerRepository;

    public Timer fetchTimer() {
        // timerRepository에서 해당 태스크에 대한 타이머 조회 후 리턴
        return null;
    }

}
