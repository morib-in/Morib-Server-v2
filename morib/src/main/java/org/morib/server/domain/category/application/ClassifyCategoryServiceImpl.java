package org.morib.server.domain.category.application;

import lombok.RequiredArgsConstructor;
import org.morib.server.api.homeView.vo.CategoriesByDate;
import org.morib.server.api.homeView.vo.CombinedByDate;
import org.morib.server.domain.category.CategoryManager;
import org.morib.server.domain.category.infra.Category;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ClassifyCategoryServiceImpl implements ClassifyCategoryService{
    private final CategoryManager categoryManager;

    @Override
    public List<CategoriesByDate> classifyByDate(List<Category> categories, LocalDate startDate, LocalDate endDate) {
        return categoryManager.classifyByDate(categories, startDate, endDate);
    }

    @Override
    public List<CombinedByDate> classifyTaskByCategory(List<CategoriesByDate> categories) {
        return categories.stream().map(categoryManager::classifyTaskByCategory).toList();
    }
}
