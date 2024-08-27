package org.morib.server.domain.category.application;

import org.morib.server.domain.category.infra.Category;

import java.time.LocalDate;
import java.util.List;
import org.morib.server.domain.user.infra.User;

public interface FetchCategoryService {
    List<Category> fetchByUserIdInRange(Long userId, LocalDate startDate, LocalDate endDate);


    Category fetchByUserAndCategoryId(User findUser, Long categoryId);
}
