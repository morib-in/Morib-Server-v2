package org.morib.server.domain.timer.application;

import org.morib.server.domain.task.infra.Task;
import org.morib.server.domain.timer.infra.Timer;
import org.morib.server.domain.user.infra.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface FetchTimerService {
    Timer fetchByTaskAndTargetDate(Task findTask, LocalDate localDate);
    int fetchElapsedTimeOrZeroByTaskAndTargetDate(Task findTask, LocalDate targetDate);
    int sumOneTaskElapsedTimeInTargetDate(Task t, LocalDate targetDate);
    List<Timer> fetchByUserAndTargetDate(User user, LocalDate targetDate);
    int sumElapsedTimeByUser(User user, LocalDate targetDate);
    Set<Long> fetchExistingTaskIdsByTargetDate(List<Task> tasks, LocalDate targetDate);
    Timer fetchByTaskIdAndTargetDate(Long taskId, LocalDate targetDate);
    Timer fetchOrCreateByTaskAndTargetDate(User user, Task task, LocalDate targetDate);

}
