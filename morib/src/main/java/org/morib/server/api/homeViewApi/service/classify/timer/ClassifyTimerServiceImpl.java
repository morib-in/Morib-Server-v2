package org.morib.server.api.homeViewApi.service.classify.timer;

import lombok.RequiredArgsConstructor;
import org.morib.server.domain.category.infra.Category;
import org.morib.server.domain.task.infra.Task;
import org.morib.server.domain.timer.TimerManager;
import org.morib.server.domain.timer.infra.Timer;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class ClassifyTimerServiceImpl implements ClassifyTimerService{
    private final TimerManager timerManager;

    @Override
    public Map<Task, Timer> classifyByTask(List<Task> tasks) {
        timerManager.classifyByTask();
    }
}
