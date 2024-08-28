package org.morib.server.domain.category.application;

import lombok.RequiredArgsConstructor;
import org.morib.server.domain.category.infra.CategoryRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteCategoryServiceImpl implements DeleteCategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public void deleteById(Long categoryId) {
        categoryRepository.findById(categoryId).orElseThrow(
                () -> new IllegalArgumentException("삭제하려는 카테고리가 존재하지 않습니다."));
        categoryRepository.deleteById(categoryId);
    }
}
