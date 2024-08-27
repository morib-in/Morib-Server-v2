package org.morib.server.domain.category.application;

import lombok.RequiredArgsConstructor;
import org.morib.server.domain.category.infra.Category;
import org.morib.server.domain.category.infra.CategoryRepository;
import org.morib.server.domain.user.infra.User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class FetchCategoryServiceImpl implements FetchCategoryService{
    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> fetchByUserIdInRange(Long userId, LocalDate startDate, LocalDate endDate) {
        return categoryRepository.findByUserIdInRange(userId, startDate, endDate);
    }

    @Override
    public Category fetchByUserAndCategoryId(User findUser, Long categoryId) {
        return categoryRepository.findByUserAndId(findUser, categoryId).
            orElseThrow(() -> new IllegalArgumentException("해당 카테고리가 없습니다."));
    }

}
