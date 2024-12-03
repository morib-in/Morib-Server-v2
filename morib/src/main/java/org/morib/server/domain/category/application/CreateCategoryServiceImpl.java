package org.morib.server.domain.category.application;

import lombok.RequiredArgsConstructor;
import org.morib.server.domain.category.infra.Category;
import org.morib.server.domain.category.infra.CategoryRepository;
import org.morib.server.domain.user.infra.User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateCategoryServiceImpl implements CreateCategoryService{
    private final CategoryRepository categoryRepository;

    @Override
    public Category create(String name, User user) {
        return categoryRepository.save(Category.create(name, user));
    }

}
