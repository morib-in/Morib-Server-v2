package org.morib.server.api.homeView.dto.fetch;

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
