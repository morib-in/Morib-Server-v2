package org.morib.server.api.homeViewApi.service.fetch.timer;

import lombok.RequiredArgsConstructor;
import org.morib.server.domain.task.TaskReader;
import org.morib.server.domain.timer.TimerAggregator;
import org.morib.server.domain.timer.TimerReader;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FetchUserTimerServiceImpl implements FetchUserTimerService{
    private final TaskReader taskReader;
    private final TimerReader timerReader;
    private final TimerAggregator timerAggregator;

    @Override
    public void execute() {
        taskReader.fetch();
        timerReader.fetch();
        timerAggregator.aggregate();
    }
}
