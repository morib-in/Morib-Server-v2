package org.morib.server.api.modalView.facade;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.morib.server.annotation.Facade;
import org.morib.server.api.modalView.dto.AllowedSiteByCategoryResponseDto;
import org.morib.server.api.modalView.dto.CreateCategoryRequestDto;
import org.morib.server.api.modalView.vo.CategoryInfoInAllowedSite;
import org.morib.server.api.modalView.vo.AllowSiteForCalledByCatgory;
import org.morib.server.domain.allowedSite.application.CreateAllowedSiteService;
import org.morib.server.domain.allowedSite.application.FetchAllowedSiteService;
import org.morib.server.domain.allowedSite.infra.AllowedSite;
import org.morib.server.domain.allowedSite.infra.type.OwnerType;
import org.morib.server.domain.category.application.CreateCategoryService;
import org.morib.server.domain.category.application.FetchCategoryService;
import org.morib.server.domain.category.infra.Category;
import org.morib.server.domain.user.application.FetchUserService;
import org.morib.server.domain.user.infra.User;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Facade
public class ModalViewFacade {

    private final FetchUserService fetchUserService;
    private final CreateCategoryService createCategoryService;
    private final FetchCategoryService fetchCategoryService;
    private final CreateAllowedSiteService createAllowedSiteService;
    private final FetchAllowedSiteService fetchAllowedSiteService;

    @Transactional
    public void createCategory(Long userId, CreateCategoryRequestDto createCategoryRequestDto) {
        User user = fetchUserService.fetchByUserId(userId);
        Category createdCategory = createCategoryService.create(createCategoryRequestDto.name(),
            createCategoryRequestDto.startDate(), createCategoryRequestDto.endDate(), user);
        createCategoryRequestDto.allowedSites().stream().map(
            allowedSite -> createAllowedSiteService.create(allowedSite.getSiteName(),
                allowedSite.getSiteUrl(), OwnerType.CATEGORY, createdCategory.getId()));
    }

    @Transactional
    public AllowedSiteByCategoryResponseDto fetchAllowedSiteByCategoryId(Long mockUserId,
        Long categoryId) {
        User user = fetchUserService.fetchByUserId(mockUserId);
        Category findCategory = fetchCategoryService.fetchByIdAndUser(categoryId, user);
        List<AllowedSite> allowedSites = fetchAllowedSiteService.fetchByCategoryId(categoryId);
        CategoryInfoInAllowedSite category = CategoryInfoInAllowedSite.of(categoryId,
            findCategory);
        List<AllowSiteForCalledByCatgory> msetList = mappedByMsetInfosAllowedSite(allowedSites);

        return AllowedSiteByCategoryResponseDto.of(category, msetList);
    }

    private static List<AllowSiteForCalledByCatgory> mappedByMsetInfosAllowedSite(
        List<AllowedSite> allowedSites) {
        return allowedSites.stream().map(AllowSiteForCalledByCatgory::of)
            .toList();
    }

}
