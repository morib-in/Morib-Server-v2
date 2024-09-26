package org.morib.server.api.homeView.vo;

import org.morib.server.domain.category.infra.Category;

import java.time.LocalDate;

public record CategoryInfo(
        Long id,
        String name
) {
    public static CategoryInfo of(Category category) {
        return new CategoryInfo(
                category.getId(),
                category.getName());
    }
}

