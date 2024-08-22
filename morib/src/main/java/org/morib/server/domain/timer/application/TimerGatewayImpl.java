package org.morib.server.domain.timer.application;

import lombok.RequiredArgsConstructor;
import org.morib.server.domain.timer.infra.Timer;
import org.morib.server.domain.timer.infra.TimerRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TimerGatewayImpl implements TimerGateway{
    private final TimerRepository timerRepository;

    @Override
    public void save() {

    }

    @Override
    public void findById() {

    }

    @Override
    public void findAll() {

    }

    @Override
    public void deleteById() {

    }

    @Override
    public void deleteAll() {

    }

    public Timer fetchTimer() {
        // timerRepository에서 해당 태스크에 대한 타이머 조회 후 리턴
        return null;
    }

}

