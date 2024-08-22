package org.morib.server.api.homeViewApi.service.classify.task;

import lombok.RequiredArgsConstructor;
import org.morib.server.domain.task.TaskManager;
import org.morib.server.domain.task.infra.Task;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@RequiredArgsConstructor
@Service
public class ClassifyTaskServiceImpl implements ClassifyTaskService {
    private final TaskManager taskManager;

    @Override
    public int classifyTimerByTask(LocalDate date, Task task) {
        return taskManager.classifyTimerByTask(date, task);
    }

}
