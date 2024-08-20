package org.morib.server.api.homeViewApi.dto.fetchHome;

import java.time.LocalDate;

public record TaskWithTimerForFetchHomeView(
        Long id,
        String name,
        LocalDate startDate,
        LocalDate endDate,
        int targetTime,
        boolean isComplete
) {
    public static TaskWithTimerForFetchHomeView of(Long id, String name, LocalDate startDate, LocalDate endDate, int targetTime, boolean isComplete) {
        return new TaskWithTimerForFetchHomeView(id, name, startDate, endDate, targetTime, isComplete);
    }
}

