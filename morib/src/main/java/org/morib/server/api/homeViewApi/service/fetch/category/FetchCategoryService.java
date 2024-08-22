package org.morib.server.api.homeViewApi.service.fetch.category;

import org.morib.server.api.homeViewApi.dto.fetch.HomeViewRequestDto;
import org.morib.server.domain.category.infra.Category;

import java.time.LocalDate;
import java.util.List;

public interface FetchCategoryService {
    List<Category> fetch(HomeViewRequestDto request);
}
