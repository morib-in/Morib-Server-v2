package org.morib.server.api.homeViewApi.dto.fetch;

import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

public record HomeViewResponseDto (
        LocalDate date,
        List<FetchCombinedDto> combinedCategoryAndTask
) {
    public static HomeViewResponseDto of(LocalDate date,
                          List<FetchCombinedDto> combinedCategoryAndTask) {
        return new HomeViewResponseDto(date, combinedCategoryAndTask);
    }
}

