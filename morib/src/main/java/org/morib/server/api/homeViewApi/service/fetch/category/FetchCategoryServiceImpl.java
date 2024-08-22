package org.morib.server.api.homeViewApi.service.fetch.category;

import lombok.RequiredArgsConstructor;
import org.morib.server.api.homeViewApi.dto.fetch.HomeViewRequestDto;
import org.morib.server.domain.category.application.CategoryGateway;
import org.morib.server.domain.category.infra.Category;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class FetchCategoryServiceImpl implements FetchCategoryService{
    private final CategoryGateway categoryGateway;

    @Override
    public List<Category> fetch(HomeViewRequestDto request) {
        return categoryGateway.findByUserIdInRange(request);
    }
}
