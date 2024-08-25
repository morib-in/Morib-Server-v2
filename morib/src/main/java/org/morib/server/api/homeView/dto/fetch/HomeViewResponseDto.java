package org.morib.server.api.homeView.dto.fetch;

import java.time.LocalDate;
import java.util.List;

public record HomeViewResponseDto (
        LocalDate date,
        List<FetchCombinedDto> fetchCombinedDtos
) {
    public static HomeViewResponseDto of(LocalDate date, List<FetchCombinedDto> fetchCombinedDtos) {
        return new HomeViewResponseDto(date, fetchCombinedDtos);
    }
}

