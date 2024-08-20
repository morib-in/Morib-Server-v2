package org.morib.server.api.timerView.service;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.morib.server.annotation.Facade;
import org.morib.server.api.timerView.dto.TaskInTodoCardDto;
import org.morib.server.api.timerView.service.fetch.todo.FetchTodoService;
import org.morib.server.api.timerView.service.stop.timer.StopTimerService;

@Facade
@RequiredArgsConstructor
public class TimerViewFacade {

    private final FetchTodoService fetchTodoService;
    private final StopTimerService stopTimerService;

    /**
     *  타이머를 멈추고 fetch를 한다.
     *  이전 코드의 로직을 참고하여 진행한다.
     */
    public void stopTimerAndFetch() {
        fetchTodoService.fetch(LocalDate.now());
        stopTimerService.stop();
    }

    /**
     *  todoCard를 가져온다.
     *  이전 코드의 로직을 참고하여 진행한다.
     * @param targetDate
     */
    public TaskInTodoCardDto getTodoCard(LocalDate targetDate) {
        return null;
    }


}
