package org.morib.server.api.homeView.vo;

import lombok.Value;
import org.morib.server.domain.task.infra.Task;

import java.time.LocalDate;

@Value
public class TaskWithElapsedTime {
    LocalDate date;
    Task task;
    int elapsedTime;

    public static TaskWithElapsedTime of(LocalDate date, Task task, int elapsedTime) {
        return new TaskWithElapsedTime(date, task, elapsedTime);
    }
}
