package org.morib.server.api.homeView.dto.fetch;

import java.time.LocalDate;

public record FetchMyElapsedTimeResponseDto(
        LocalDate targetDate,
        int sumTodayElapsedTime
) {
    public static FetchMyElapsedTimeResponseDto of(LocalDate targetDate,  int sumTodayElapsedTime){
        return new FetchMyElapsedTimeResponseDto(targetDate, sumTodayElapsedTime);
    }
}
