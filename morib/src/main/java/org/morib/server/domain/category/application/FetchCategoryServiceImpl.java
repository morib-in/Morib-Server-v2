package org.morib.server.domain.category.application;

import lombok.RequiredArgsConstructor;
import org.morib.server.api.homeView.vo.CategoryWithTasks;
import org.morib.server.api.homeView.vo.TaskWithTimers;
import org.morib.server.domain.category.CategoryManager;
import org.morib.server.domain.category.infra.Category;
import org.morib.server.domain.category.infra.CategoryRepository;
import org.morib.server.domain.user.infra.User;
import org.morib.server.global.exception.BusinessException;
import org.morib.server.global.message.ErrorMessage;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class FetchCategoryServiceImpl implements FetchCategoryService{
    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> fetchByUserIdInRange(Long userId, LocalDate startDate, LocalDate endDate) {
        return categoryRepository.findByUserIdInRange(userId, startDate, endDate);
    }

    @Override
    public Set<Category> fetchByUser(User user) {
        return user.getCategories();
    }

    @Override
    public CategoryWithTasks convertToCategoryWithTasks(Category category, LinkedHashSet<TaskWithTimers> taskWithTimers) {
        return CategoryWithTasks.of(category, taskWithTimers);
    }

    @Override
    public Category fetchByUserAndCategoryId(User findUser, Long categoryId) {
        return categoryRepository.findByUserAndId(findUser, categoryId).
            orElseThrow(() -> new BusinessException(ErrorMessage.NOT_FOUND));
    }
}
