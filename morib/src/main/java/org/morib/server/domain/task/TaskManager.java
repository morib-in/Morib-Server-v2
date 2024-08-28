package org.morib.server.domain.task;

import lombok.RequiredArgsConstructor;
import org.morib.server.domain.task.infra.Task;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TaskManager {
    public void toggleTaskStatus(Task findTask) {
        findTask.toggleStatus();
    }
}
