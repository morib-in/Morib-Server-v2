package org.morib.server.api.homeViewApi.service.toggle;

import lombok.RequiredArgsConstructor;
import org.morib.server.domain.task.application.TaskManager;
import org.morib.server.domain.task.infra.TaskGateway;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ToggleTaskStatusServiceImpl implements ToggleTaskStatusService{
    private final TaskGateway taskGateway;
    private final TaskManager taskManager;

    @Override
    public void execute() {
        // gateway로 task 꺼내와서
        // switch로 상태 변경 후
        // gateway로 변경된 task 저장
    }
}
