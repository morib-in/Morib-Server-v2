package org.morib.server.domain.category.application;

import org.morib.server.domain.category.infra.Category;
import org.morib.server.domain.user.infra.User;

public interface CreateCategoryService {
    Category create(String name, User user);
}
