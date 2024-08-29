package org.morib.server.domain.category.application;

import org.morib.server.api.homeView.vo.CategoryWithTasks;
import org.morib.server.api.homeView.vo.TaskWithTimers;
import org.morib.server.domain.category.infra.Category;
import org.morib.server.domain.user.infra.User;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;
import org.morib.server.domain.user.infra.User;
import java.util.Set;

public interface FetchCategoryService {
    List<Category> fetchByUserIdInRange(Long userId, LocalDate startDate, LocalDate endDate);
    Set<Category> fetchByUser(User user);
    CategoryWithTasks convertToCategoryWithTasks(Category category, LinkedHashSet<TaskWithTimers> taskWithTimers);

    Category fetchByIdAndUser(Long categoryId, User user);

    Category fetchByUserAndCategoryId(User findUser, Long categoryId);
}
