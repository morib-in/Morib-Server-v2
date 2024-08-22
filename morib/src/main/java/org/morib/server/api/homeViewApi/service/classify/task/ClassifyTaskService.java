package org.morib.server.api.homeViewApi.service.classify.task;

import org.morib.server.domain.category.infra.Category;
import org.morib.server.domain.task.infra.Task;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ClassifyTaskService {
    int classifyTimerByTask(LocalDate date, Task task);
}
