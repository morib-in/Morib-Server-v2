package org.morib.server.api.timerView.service.fetch.timer;

import java.time.LocalDate;
import java.util.Set;

import org.morib.server.domain.task.infra.Task;
import org.morib.server.domain.timer.infra.Timer;

public interface FetchTimerService {
    void fetch();

    Timer fetchByTaskAndTargetDate(Task findTask, LocalDate localDate);

    void addElapsedTime(Timer timer, int elapsedTime);

    Long sumTasksElapsedTimeByTargetDate(Set<Task> tasks, LocalDate targetDate);

    Long sumOneTaskElapsedTimeInTargetDate(Task t, LocalDate targetDate);
}
