package org.morib.server.domain.category.application;

import lombok.RequiredArgsConstructor;
import org.morib.server.domain.category.CategoryManager;
import org.morib.server.domain.category.infra.Category;
import org.morib.server.domain.task.infra.Task;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class ClassifyCategoryServiceImpl implements ClassifyCategoryService{
    private final CategoryManager categoryManager;

    @Override
    public Map<LocalDate, List<Category>> classifyByDate(List<Category> categories, LocalDate startDate, LocalDate endDate) {
        return categoryManager.classifyByDate(categories, startDate, endDate);
    }

    @Override
    public Map<LocalDate, Map<Category, List<Task>>> classifyTaskByCategory(Map<LocalDate, List<Category>> categories) {
        return categoryManager.classifyTaskByCategory(categories);
    }
}
