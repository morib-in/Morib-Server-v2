package org.morib.server.domain.task;

import lombok.RequiredArgsConstructor;
import org.morib.server.domain.task.infra.Task;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TaskManager {

    public boolean isTaskHaveSameTaskId(Task task, Long taskId) {
        return task.getId().equals(taskId);
    }
}
