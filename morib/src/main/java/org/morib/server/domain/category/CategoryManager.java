package org.morib.server.domain.category;

import org.morib.server.annotation.Manager;
import org.morib.server.api.homeView.vo.CategoriesByDate;
import org.morib.server.api.homeView.vo.CombinedByDate;
import org.morib.server.api.homeView.vo.TasksByCategory;
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

    public CombinedByDate classifyTaskByCategory(CategoriesByDate categoriesByDate) {
        LocalDate idxDate = categoriesByDate.getDate();
        List<TasksByCategory> tasksByCategory = new ArrayList<>();
        for (Category category : categoriesByDate.getCategories()) {
            List<Task> tasks = new ArrayList<>();
            for (Task task : category.getTasks()) {
                if (isInRange(idxDate, task.getStartDate(), task.getEndDate())) {
                    tasks.add(task);
                }
            }
            tasksByCategory.add(TasksByCategory.of(category, tasks));
        }
        return CombinedByDate.of(idxDate, tasksByCategory);
    }

    private boolean isInRange(LocalDate idxDate, LocalDate startDate, LocalDate endDate) {
        return (idxDate.isEqual(startDate) || idxDate.isAfter(startDate)) &&
                (idxDate.isEqual(endDate) || idxDate.isBefore(endDate));
    }
}
