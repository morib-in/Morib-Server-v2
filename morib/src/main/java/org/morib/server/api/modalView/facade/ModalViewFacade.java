package org.morib.server.api.modalView.facade;

import lombok.RequiredArgsConstructor;
import org.morib.server.annotation.Facade;
import org.morib.server.api.modalView.dto.CreateCategoryRequest;
import org.morib.server.domain.allowedSite.AllowedSiteManager;
import org.morib.server.domain.allowedSite.application.CreateAllowedSiteService;
import org.morib.server.domain.allowedSite.infra.type.OwnerType;
import org.morib.server.domain.category.CategoryManager;
import org.morib.server.domain.category.application.CreateCategoryService;
import org.morib.server.domain.category.infra.Category;
import org.morib.server.domain.user.application.FetchUserService;
import org.morib.server.domain.user.infra.User;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Facade
public class ModalViewFacade {

    private final FetchUserService fetchUserService;
    private final CreateCategoryService createCategoryService;
    private final CreateAllowedSiteService createAllowedSiteService;

    @Transactional
    public void createCategory(Long userId, CreateCategoryRequest createCategoryRequest) {
        User user = fetchUserService.fetchByUserId(userId);
        Category createdCategory = createCategoryService.create(createCategoryRequest.name(), createCategoryRequest.startDate(), createCategoryRequest.endDate(), user);
        createCategoryRequest.allowedSites().stream().map(
                allowedSite -> createAllowedSiteService.create(allowedSite.getSiteName(), allowedSite.getSiteUrl(), OwnerType.CATEGORY, createdCategory.getId()));
    }
}
