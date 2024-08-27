package org.morib.server.api.modalView.vo;

import java.time.LocalDate;
import org.morib.server.domain.category.infra.Category;

public record CategoryInfoInAllowedSite(
    Long id,
    String name,
    LocalDate startDate,
    LocalDate endDate
) {

    public static CategoryInfoInAllowedSite of(Long categoryId, Category category) {
        return new CategoryInfoInAllowedSite( categoryId, category.getName(), category.getStartDate(), category.getEndDate());
    }
}
