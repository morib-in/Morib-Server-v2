package org.morib.server.api.homeViewApi.dto.fetchHome;

import java.time.LocalDate;

public record CategoryForFetchHomeView(
        Long id,
        String name,
        LocalDate startDate,
        LocalDate endDate) {
    public static CategoryForFetchHomeView of(Long id, String name, LocalDate startDate, LocalDate endDate) {
        return new CategoryForFetchHomeView(id, name, startDate, endDate);
    }
}

