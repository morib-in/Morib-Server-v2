package org.morib.server.domain.task;

import lombok.RequiredArgsConstructor;
import org.morib.server.annotation.Manager;
import org.morib.server.domain.task.infra.Task;

@RequiredArgsConstructor
@Manager
public class TaskManager {
    public void toggleTaskStatus(Task findTask) {
        findTask.toggleStatus();
    }
}
