package org.morib.server.domain.category;

import org.morib.server.annotation.Manager;
import org.morib.server.domain.category.infra.Category;

@Manager
public class CategoryManager {

    public void updateName(Category category, String name) {
        category.updateName(name);
    }
}
