package org.morib.server.domain.category.application;

import lombok.RequiredArgsConstructor;
import org.morib.server.api.homeViewApi.dto.fetch.HomeViewRequestDto;
import org.morib.server.domain.category.infra.Category;
import org.morib.server.domain.category.infra.CategoryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CategoryGatewayImpl implements CategoryGateway{
    private final CategoryRepository categoryRepository;

    @Override
    public void save() {

    }

    @Override
    public void findById() {

    }

    @Override
    public void findAll() {

    }

    @Override
    public void deleteById() {

    }

    @Override
    public void deleteAll() {

    }

    public List<Category> findByUserIdInRange(HomeViewRequestDto request) {
        return categoryRepository.findByUserIdInRange(request.userId(), request.startDate(), request.endDate());
    }
}
