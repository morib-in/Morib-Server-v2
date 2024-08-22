package org.morib.server.api.timerView.service.fetch.timer;

import java.time.LocalDate;
import org.morib.server.domain.task.infra.Task;
import org.morib.server.domain.timer.infra.Timer;

public interface FetchTimerService {
    void fetch();

    Timer fetchByTaskAndTargetDate(Task findTask, LocalDate localDate);

    void addElapsedTime(Timer timer, int elapsedTime);
}
