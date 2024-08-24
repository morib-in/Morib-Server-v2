package org.morib.server.domain.task.application;

import lombok.RequiredArgsConstructor;
import org.morib.server.domain.task.TaskManager;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ToggleTaskStatusServiceImpl implements ToggleTaskStatusService {
    private final TaskManager taskManager;

    @Override
    public void toggle() {
        // operator로 상태 변경
    }
}
