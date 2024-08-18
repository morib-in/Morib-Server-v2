package org.morib.server.api.modalViewApi.service;

import lombok.RequiredArgsConstructor;
import org.morib.server.api.modalViewApi.service.create.CreateCategoryService;
import org.morib.server.api.modalViewApi.service.delete.DeleteCategoryService;
import org.morib.server.api.modalViewApi.service.fetch.FetchCategoryService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ModalViewFacade {
    private final CreateCategoryService createCategoryService;
    private final FetchCategoryService fetchCategoryService;
    private final DeleteCategoryService deleteCategoryService;

    public void createCategory() {
        createCategoryService.execute();
    }

    public void fetchCategories() {
        fetchCategoryService.execute();
    }

    public void deleteCategory() {
        deleteCategoryService.execute();
    }
}
