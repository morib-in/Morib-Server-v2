package org.morib.server.api.homeViewApi.service;

import lombok.RequiredArgsConstructor;
import org.morib.server.api.homeViewApi.service.create.CreateTaskService;
import org.morib.server.api.homeViewApi.service.fetch.home.FetchHomeService;
import org.morib.server.api.homeViewApi.service.fetch.timer.FetchUserTimerService;
import org.morib.server.api.homeViewApi.service.start.StartTimerService;
import org.morib.server.api.homeViewApi.service.toggle.ToggleTaskStatusService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class HomeViewFacade {

    private final FetchHomeService fetchHomeService;
    private final FetchUserTimerService fetchUserTimerService;
    private final CreateTaskService createTaskService;
    private final ToggleTaskStatusService toggleTaskStatusService;
    private final StartTimerService startTimerService;

    public void fetchHome() {
        fetchHomeService.execute();
    }

    public void fetchUserTimer() {
        fetchUserTimerService.execute();
    }

    public void createTask() {
        createTaskService.execute();
    }

    public void toggleTaskStatus() {
        toggleTaskStatusService.execute();
    }

    public void startTimer() {
        startTimerService.execute();
    }
}
