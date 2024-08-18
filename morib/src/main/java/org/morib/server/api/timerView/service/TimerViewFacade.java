package org.morib.server.api.timerView.service;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.morib.server.annotation.Facade;
import org.morib.server.api.timerView.dto.TaskInTodoCard;
import org.morib.server.api.timerView.service.fetch.FetchTimerService;
import org.morib.server.api.timerView.service.stop.StopTimerService;

@Facade
@RequiredArgsConstructor
public class TimerViewFacade {

    private final FetchTimerService fetchTimerService;
    private final StopTimerService stopTimerService;

    /**
     *  타이머를 멈추고 fetch를 한다.
     *  이전 코드의 로직을 참고하여 진행한다.
     */
    public void stopTimerAndFetch() {
        fetchTimerService.fetch();
        stopTimerService.stop();
    }

    /**
     *  todoCard를 가져온다.
     *  이전 코드의 로직을 참고하여 진행한다.
     * @param targetDate
     */
    public TaskInTodoCard getTodoCard(LocalDate targetDate) {
        return null;
    }


}
