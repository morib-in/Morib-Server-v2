package org.morib.server.domain.timer.application;

import org.morib.server.domain.task.infra.Task;
import org.morib.server.domain.user.infra.User;

import java.time.LocalDate;

public interface CreateTimerService {
    void createTimer(User user, LocalDate targetDate, Task task);
}
