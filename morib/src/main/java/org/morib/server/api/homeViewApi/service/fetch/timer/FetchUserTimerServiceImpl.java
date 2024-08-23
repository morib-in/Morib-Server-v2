package org.morib.server.api.homeViewApi.service.fetch.timer;

import lombok.RequiredArgsConstructor;
import org.morib.server.domain.task.infra.TaskGateway;
import org.morib.server.domain.timer.application.TimerManager;
import org.morib.server.domain.timer.infra.TimerGateway;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FetchUserTimerServiceImpl implements FetchUserTimerService{
    private final TaskGateway taskGateway;
    private final TimerGateway timerGateway;
    private final TimerManager timerManager;

    @Override
    public void execute() {
        taskGateway.fetchTasksInRange();
        timerGateway.fetchTimer();
        timerManager.aggregate();
    }
}
