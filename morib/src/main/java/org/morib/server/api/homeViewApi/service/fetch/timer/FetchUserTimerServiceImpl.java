package org.morib.server.api.homeViewApi.service.fetch.timer;

import lombok.RequiredArgsConstructor;
import org.morib.server.domain.task.infra.TaskGateway;
import org.morib.server.domain.timer.TimerOperator;
import org.morib.server.domain.timer.infra.TimerGateway;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FetchUserTimerServiceImpl implements FetchUserTimerService{
    private final TaskGateway taskGateway;
    private final TimerGateway timerGateway;
    private final TimerOperator timerOperator;

    @Override
    public void execute() {
        taskGateway.fetchTasksInRange();
        timerGateway.fetchTimer();
        timerOperator.aggregate();
    }
}
