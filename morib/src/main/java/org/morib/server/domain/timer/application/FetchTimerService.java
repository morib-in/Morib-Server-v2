package org.morib.server.domain.timer.application;

import java.time.LocalDate;
import java.util.Set;

import org.morib.server.domain.task.infra.Task;
import org.morib.server.domain.timer.infra.Timer;
import org.morib.server.domain.user.infra.User;

public interface FetchTimerService {
    void fetch();

    Timer fetchByTaskAndTargetDate(Task findTask, LocalDate localDate);
    

    int sumOneTaskElapsedTimeInTargetDate(Task t, LocalDate targetDate);

    int sumElapsedTimeByUser(User user, LocalDate targetDate);
}
