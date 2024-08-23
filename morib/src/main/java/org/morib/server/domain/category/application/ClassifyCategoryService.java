package org.morib.server.domain.category.application;

import org.morib.server.api.homeViewApi.vo.CategoriesByDate;
import org.morib.server.api.homeViewApi.vo.CombinedByDate;
import org.morib.server.domain.category.infra.Category;
import org.morib.server.domain.task.infra.Task;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ClassifyCategoryService {
    List<CategoriesByDate> classifyByDate(List<Category> categories, LocalDate startDate, LocalDate endDate);
    List<CombinedByDate> classifyTaskByCategory(List<CategoriesByDate> categories);
}
