package org.morib.server.api.homeView.vo;

import org.morib.server.domain.category.infra.Category;

import java.time.LocalDate;

public record CategoryInfo(
        Long id,
        String name,
        LocalDate startDate,
        LocalDate endDate
) {
    public static CategoryInfo of(Category category) {
        return new CategoryInfo(
                category.getId(),
                category.getName(),
                category.getStartDate(),
                category.getEndDate());
    }
}

