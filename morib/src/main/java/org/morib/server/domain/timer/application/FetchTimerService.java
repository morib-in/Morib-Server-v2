package org.morib.server.domain.timer.application;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import org.morib.server.domain.task.infra.Task;
import org.morib.server.domain.timer.infra.Timer;

public interface FetchTimerService {
    void fetch();

    Timer fetchByTaskAndTargetDate(Task findTask, LocalDate localDate);

    void addElapsedTime(Timer timer, int elapsedTime);

    int sumTasksElapsedTimeByTargetDate(LinkedHashSet<Task> tasks, LocalDate targetDate);

    Object sumOneTaskElapsedTimeInTargetDate(Task task, LocalDate targetDate);
}
