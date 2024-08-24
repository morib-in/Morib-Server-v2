package org.morib.server.domain.category.application;

import org.morib.server.api.homeView.vo.CategoriesByDate;
import org.morib.server.api.homeView.vo.CombinedByDate;
import org.morib.server.domain.category.infra.Category;

import java.time.LocalDate;
import java.util.List;

public interface ClassifyCategoryService {
    List<CategoriesByDate> classifyByDate(List<Category> categories, LocalDate startDate, LocalDate endDate);
    List<CombinedByDate> classifyTaskByCategory(List<CategoriesByDate> categories);
}
