package org.morib.server.domain.timer.application;

import java.time.LocalDate;
import java.util.Set;

import org.morib.server.domain.task.infra.Task;
import org.morib.server.domain.timer.infra.Timer;

public interface FetchTimerService {
    void fetch();

    Timer fetchByTaskAndTargetDate(Task findTask, LocalDate localDate);

    int fetchElapsedTimeOrZeroByTaskAndTargetDate(Task findTask, LocalDate targetDate);

    int sumTasksElapsedTimeByTargetDate(Set<Task> tasks, LocalDate targetDate);

    int sumOneTaskElapsedTimeInTargetDate(Task t, LocalDate targetDate);
}
