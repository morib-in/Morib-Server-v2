package org.morib.server.api.homeViewApi.service.create.timer;

import lombok.RequiredArgsConstructor;
import org.morib.server.domain.timer.application.TimerGateway;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CreateTimerServiceImpl implements CreateTimerService{
    private final TimerGateway timerGateway;

    @Override
    public void create() {

    }
}
