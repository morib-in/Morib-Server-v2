package org.morib.server.api.homeViewApi.dto.fetch;

import java.time.LocalDate;

public record HomeViewRequestDto(
        Long userId,
        LocalDate startDate,
        LocalDate endDate
) {
    public static HomeViewRequestDto of(Long userId, LocalDate startDate, LocalDate endDate) {
        return new HomeViewRequestDto(userId, startDate, endDate);
    }
}
