package org.morib.server.api.homeViewApi.service;

import lombok.RequiredArgsConstructor;
import org.morib.server.api.homeViewApi.service.fetch.home.FetchHomeService;
import org.morib.server.api.homeViewApi.service.fetch.timer.FetchUserTimerService;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class HomeViewFacade {

    private final FetchHomeService fetchHomeService;
    private final FetchUserTimerService fetchUserTimerService;

    public void fetchHome() {
        fetchHomeService.execute();
    }

    public void fetchUserTimer() {
        fetchUserTimerService.execute();
    }
}
