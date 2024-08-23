package org.morib.server.domain.timer.application;

import org.morib.server.domain.category.infra.Category;
import org.morib.server.domain.task.infra.Task;
import org.morib.server.domain.timer.infra.Timer;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ClassifyTimerService {
    int classifyTaskByDate(LocalDate date, Task task);
}
