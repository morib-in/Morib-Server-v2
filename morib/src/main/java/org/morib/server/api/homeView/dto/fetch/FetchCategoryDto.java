package org.morib.server.api.homeView.dto.fetch;

import org.morib.server.domain.category.infra.Category;

import java.time.LocalDate;

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

