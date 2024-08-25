package org.morib.server.domain.category.application;

import lombok.RequiredArgsConstructor;
import org.morib.server.api.homeView.vo.CategoryWithTasks;
import org.morib.server.api.homeView.vo.TaskWithTimers;
import org.morib.server.domain.category.CategoryManager;
import org.morib.server.domain.category.infra.Category;
import org.morib.server.domain.category.infra.CategoryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;

@RequiredArgsConstructor
@Service
public class FetchCategoryServiceImpl implements FetchCategoryService{
    private final CategoryRepository categoryRepository;
    private final CategoryManager categoryManager;


    @Override
    public List<Category> fetchByUserIdInRange(Long userId, LocalDate startDate, LocalDate endDate) {
        return categoryRepository.findByUserIdInRange(userId, startDate, endDate);
    }

    @Override
    public CategoryWithTasks convertToCategoryWithTasks(Category category, LinkedHashSet<TaskWithTimers> taskWithTimers) {
        return CategoryWithTasks.of(category, taskWithTimers);
    }
}
