package org.morib.server.api.homeViewApi.service.fetch.timer;

import lombok.RequiredArgsConstructor;
import org.morib.server.domain.timer.application.TimerGateway;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FetchTimerServiceImpl implements FetchTimerService {
    private final TimerGateway timerGateway;

    @Override
    public void fetch() {

    }
}
