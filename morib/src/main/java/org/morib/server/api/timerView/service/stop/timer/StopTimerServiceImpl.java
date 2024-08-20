package org.morib.server.api.timerView.service.stop.timer;

import lombok.RequiredArgsConstructor;
import org.morib.server.domain.timer.TimerAggregator;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StopTimerServiceImpl implements StopTimerService {

    private final TimerAggregator timerAggregator;

    @Override
    public void stop() {
        //timer가 멈췄을때 계산해주는 역할을 해야함!
        timerAggregator.aggregate();
    }


}
