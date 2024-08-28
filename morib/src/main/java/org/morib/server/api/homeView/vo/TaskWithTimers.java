package org.morib.server.api.homeView.vo;

import org.morib.server.domain.task.infra.Task;
import org.morib.server.domain.timer.infra.Timer;

import java.util.Set;

public record TaskWithTimers(
        Task task,
        Set<Timer> timers
) {
    public static TaskWithTimers of(Task task, Set<Timer> timers) {
        return new TaskWithTimers(task, timers);
    }
}
