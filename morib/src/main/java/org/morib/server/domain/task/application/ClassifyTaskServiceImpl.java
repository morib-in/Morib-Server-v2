package org.morib.server.domain.task.application;

import lombok.RequiredArgsConstructor;
import org.morib.server.api.homeViewApi.vo.TaskWithElapsedTime;
import org.morib.server.domain.task.TaskManager;
import org.morib.server.domain.task.infra.Task;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ClassifyTaskServiceImpl implements ClassifyTaskService {
    private final TaskManager taskManager;

    @Override
    public List<TaskWithElapsedTime> classifyTimerByTask(LocalDate date, List<Task> tasks) {
        return tasks.stream().map(task -> taskManager.classifyTimerByTask(date, task)).toList();
    }
}
