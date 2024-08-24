package org.morib.server.domain.task.application;

import org.morib.server.api.homeView.vo.TaskWithElapsedTime;
import org.morib.server.domain.task.infra.Task;

import java.time.LocalDate;
import java.util.List;

public interface ClassifyTaskService {
    List<TaskWithElapsedTime> classifyTimerByTask(LocalDate date, List<Task> tasks);
}
