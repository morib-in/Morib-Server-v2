package org.morib.server.api.homeViewApi.service.start;

import lombok.RequiredArgsConstructor;
import org.morib.server.domain.task.infra.TaskGateway;
import org.morib.server.domain.timer.application.TimerManager;
import org.morib.server.domain.timer.infra.TimerGateway;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class StartTimerServiceImpl implements StartTimerService {
    // private final TodoGateway todoGateway;
    private final TaskGateway taskGateway;
    private final TimerGateway timerGateway;
    // private final TodoOperator todoOperator
    private final TimerManager timerManager;

    @Override
    public void execute() {
        // TodoGateway로 Todo 있는지 확인 후 생성
        // Task와 연결
        // Timer도 연결 (날짜 확인해서)
    }
}
