package org.morib.server.domain.category.application;

import org.morib.server.api.homeViewApi.dto.fetch.HomeViewRequestDto;
import org.morib.server.domain.category.infra.Category;

import java.time.LocalDate;
import java.util.List;

public interface FetchCategoryService {
    List<Category> fetchByUserIdInRange(Long userId, LocalDate startDate, LocalDate endDate);
}
