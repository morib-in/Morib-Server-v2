package org.morib.server.domain.category.application;

import org.morib.server.domain.category.infra.Category;
import org.morib.server.domain.task.infra.Task;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ClassifyCategoryService {
    Map<LocalDate, List<Category>> classifyByDate(List<Category> categories, LocalDate startDate, LocalDate endDate);
    Map<LocalDate, Map<Category, List<Task>>> classifyTaskByCategory(Map<LocalDate, List<Category>> categories);
}
