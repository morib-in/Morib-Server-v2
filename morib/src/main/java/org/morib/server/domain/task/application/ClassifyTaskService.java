package org.morib.server.domain.task.application;

import org.morib.server.domain.task.infra.Task;

import java.time.LocalDate;

public interface ClassifyTaskService {
    int classifyTimerByTask(LocalDate date, Task task);
}
