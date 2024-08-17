package org.morib.server.domain.category;

import lombok.RequiredArgsConstructor;
import org.morib.server.domain.category.infra.CategoryRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class CategoryReader {
    private final CategoryRepository categoryRepository;

    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;

    public void fetchCategories() {
        // categoryRepository에서 구간에 맞는 category 조회
    }
}
