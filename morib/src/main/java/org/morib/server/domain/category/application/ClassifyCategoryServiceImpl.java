package org.morib.server.domain.category.application;

import lombok.RequiredArgsConstructor;
import org.morib.server.domain.category.CategoryManager;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ClassifyCategoryServiceImpl implements ClassifyCategoryService{
    private final CategoryManager categoryManager;


}
