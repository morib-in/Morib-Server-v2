package org.morib.server.domain.timer.application;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.morib.server.domain.task.infra.Task;
import org.morib.server.domain.timer.infra.Timer;
import org.morib.server.domain.user.infra.User;

public interface FetchTimerService {
    Timer fetchByTaskAndTargetDate(Task findTask, LocalDate localDate);
    int fetchElapsedTimeOrZeroByTaskAndTargetDate(Task findTask, LocalDate targetDate);
    int sumOneTaskElapsedTimeInTargetDate(Task t, LocalDate targetDate);
    List<Timer> fetchByUserAndTargetDate(User user, LocalDate targetDate);
    int sumElapsedTimeByUser(User user, LocalDate targetDate);
    Set<Long> fetchExistingTaskIdsByTargetDate(List<Task> tasks, LocalDate targetDate);
    Timer fetchByTaskIdAndTargetDate(Long taskId, LocalDate targetDate);


}
