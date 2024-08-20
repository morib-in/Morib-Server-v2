package org.morib.server.domain.category.infra;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CategoryGateway {
    private final CategoryRepository categoryRepository;

    public List<Category> fetchCategoriesInRange() {
        // categoryRepository에서 구간에 맞는 카테고리 조회 후 리턴
        return null;
    }
}
