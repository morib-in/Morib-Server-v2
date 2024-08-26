package org.morib.server.api.homeView.dto.fetch;

import org.morib.server.domain.timer.infra.Timer;

import java.time.LocalDate;

public record FetchMyElapsedTimeResponseDto(
        LocalDate targetDate,
        int targetTime
) {
    public static FetchMyElapsedTimeResponseDto of(Timer timer){
        return new FetchMyElapsedTimeResponseDto(timer.getTargetDate(), timer.getElapsedTime());
    }
}
