package org.morib.server.domain.category;

import org.morib.server.annotation.Manager;
import org.morib.server.api.homeView.vo.CategoriesByDate;
import org.morib.server.api.homeView.vo.CategoryWithTasksByDate;
import org.morib.server.api.homeView.vo.CategoryWithTasks;
import org.morib.server.domain.category.infra.Category;
import org.morib.server.domain.task.infra.Task;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Manager
public class CategoryManager {

    public List<CategoriesByDate> classifyByDate(List<Category> categories, LocalDate startDate, LocalDate endDate) {
        List<CategoriesByDate> result = new ArrayList<>();
        for (LocalDate idxDate = startDate; !idxDate.isAfter(endDate); idxDate = idxDate.plusDays(1)) {
            LocalDate copiedIdxDate = idxDate;
            result.add(CategoriesByDate.of(copiedIdxDate, categories.stream()
                            .filter(category -> isInRange(copiedIdxDate, category.getStartDate(), category.getEndDate()))
                            .collect(Collectors.toList())));
        }
        return result;
    }

    public CategoryWithTasksByDate classifyTaskByCategory(CategoriesByDate categoriesByDate) {
        LocalDate idxDate = categoriesByDate.getDate();
        List<CategoryWithTasks> categoryWithTasks = new ArrayList<>();
        for (Category category : categoriesByDate.getCategories()) {
            categoryWithTasks.add(CategoryWithTasks.of(category, getTasks(category, idxDate)));
        }
        return CategoryWithTasksByDate.of(idxDate, categoryWithTasks);
    }

    private List<Task> getTasks(Category category, LocalDate idxDate) {
        List<Task> tasks = new ArrayList<>();
        for (Task task : category.getTasks()) {
            if (isInRange(idxDate, task.getStartDate(), task.getEndDate())) {
                tasks.add(task);
            }
        }
        return tasks;
    }

    private boolean isInRange(LocalDate idxDate, LocalDate startDate, LocalDate endDate) {
        return !idxDate.isBefore(startDate) && !idxDate.isAfter(endDate);
    }
    
    
}
