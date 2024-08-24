package org.morib.server.domain.timer.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FetchTimerServiceImpl implements FetchTimerService {
    private final TimerGateway timerGateway;

    @Override
    public void fetch() {

    }
}
