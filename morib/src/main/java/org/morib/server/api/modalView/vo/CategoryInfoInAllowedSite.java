package org.morib.server.api.modalView.vo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.morib.server.domain.category.infra.Category;

public record CategoryInfoInAllowedSite(
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    Long id,
    String name,
    Long userId,
    LocalDate startDate,
    LocalDate endDate
) {

    public static CategoryInfoInAllowedSite of(Long mockUserId, Long categoryId, Category category) {
        return new CategoryInfoInAllowedSite(category.getCreatedAt(), category.getUpdatedAt(), categoryId, category.getName(),
            mockUserId, category.getStartDate(), category.getEndDate());
    }
}
