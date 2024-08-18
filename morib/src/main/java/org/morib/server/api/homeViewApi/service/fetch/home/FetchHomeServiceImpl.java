package org.morib.server.api.homeViewApi.service.fetch.home;

import lombok.RequiredArgsConstructor;
import org.morib.server.domain.category.infra.CategoryGateway;
import org.morib.server.domain.task.infra.TaskGateway;
import org.morib.server.domain.timer.infra.TimerGateway;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FetchHomeServiceImpl implements FetchHomeService {

    private final CategoryGateway categoryGateway;
    private final TaskGateway taskGateway;
    private final TimerGateway timerGateway;

    @Override
    public void execute() {
        // 각 Gateway로 엔티티 조회 후 Dto Build
    }

}
