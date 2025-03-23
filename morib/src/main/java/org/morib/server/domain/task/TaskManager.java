package org.morib.server.domain.task;

import lombok.RequiredArgsConstructor;
import org.morib.server.annotation.Manager;
import org.morib.server.domain.task.infra.Task;

import java.time.LocalDate;

@RequiredArgsConstructor
@Manager
public class TaskManager {
    public void toggleTaskStatus(Task findTask) {
        findTask.toggleStatus();
    }
    public void updateTask(Task findTask, String name, LocalDate startDate, LocalDate endDate) {
        findTask.updateName(name, startDate, endDate);
    }
}
