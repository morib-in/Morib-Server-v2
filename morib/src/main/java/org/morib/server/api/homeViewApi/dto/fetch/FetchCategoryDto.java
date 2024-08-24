package org.morib.server.api.homeViewApi.dto.fetch;

import org.morib.server.domain.category.infra.Category;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record FetchCategoryDto(
        Long id,
        String name,
        LocalDate startDate,
        LocalDate endDate
) {
    public static FetchCategoryDto of(Category category) {
        return new FetchCategoryDto(
                category.getId(),
                category.getName(),
                category.getStartDate(),
                category.getEndDate());
    }
}

