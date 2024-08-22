package org.morib.server.domain.category.application;

import lombok.RequiredArgsConstructor;
import org.morib.server.api.homeViewApi.dto.fetch.HomeViewRequestDto;
import org.morib.server.domain.category.infra.Category;
import org.morib.server.domain.category.infra.CategoryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;


public interface CategoryGateway {
    void save();

    void findById();

    void findAll();

    void deleteById();

    void deleteAll();

    List<Category> findByUserIdInRange(HomeViewRequestDto request);
}
