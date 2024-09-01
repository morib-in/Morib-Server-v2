package org.morib.server.domain.category.application;

import lombok.RequiredArgsConstructor;
import org.morib.server.domain.category.infra.Category;
import org.morib.server.domain.category.infra.CategoryRepository;
import org.morib.server.global.exception.NotFoundException;
import org.morib.server.global.message.ErrorMessage;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteCategoryServiceImpl implements DeleteCategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public void deleteById(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new NotFoundException(ErrorMessage.NOT_FOUND));
        categoryRepository.delete(category);
    }
}
