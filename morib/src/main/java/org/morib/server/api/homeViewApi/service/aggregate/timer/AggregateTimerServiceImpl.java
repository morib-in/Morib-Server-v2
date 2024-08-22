package org.morib.server.api.homeViewApi.service.aggregate.timer;

import lombok.RequiredArgsConstructor;
import org.morib.server.domain.timer.TimerManager;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AggregateTimerServiceImpl implements AggregateTimerService {
    private final TimerManager timerManager;

    @Override
    public void aggregate() {

    }
}
