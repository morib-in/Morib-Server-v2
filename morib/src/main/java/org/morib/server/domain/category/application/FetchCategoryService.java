package org.morib.server.domain.category.application;

import org.morib.server.api.homeView.vo.CategoryWithTasks;
import org.morib.server.api.homeView.vo.TaskWithTimers;
import org.morib.server.domain.category.infra.Category;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;

public interface FetchCategoryService {
    List<Category> fetchByUserIdInRange(Long userId, LocalDate startDate, LocalDate endDate);

    CategoryWithTasks convertToCategoryWithTasks(Category category, LinkedHashSet<TaskWithTimers> taskWithTimers);
}
