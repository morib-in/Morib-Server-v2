package org.morib.server.domain.category.application;

import lombok.RequiredArgsConstructor;
import org.morib.server.api.homeViewApi.vo.CategoriesByDate;
import org.morib.server.api.homeViewApi.vo.CombinedByDate;
import org.morib.server.domain.category.CategoryManager;
import org.morib.server.domain.category.infra.Category;
import org.morib.server.domain.task.infra.Task;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
