package org.morib.server.api.modalView.facade;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.morib.server.annotation.Facade;
import org.morib.server.api.modalView.dto.AllowedSiteByCategoryResponseDto;
import org.morib.server.api.modalView.dto.AllowedSiteByTaskResponseDto;
import org.morib.server.api.modalView.dto.CreateCategoryRequestDto;
import org.morib.server.api.modalView.vo.AllowSiteForCalledByTask;
import org.morib.server.api.modalView.vo.CategoryInfoInAllowedSite;
import org.morib.server.api.modalView.vo.AllowSiteForCalledByCatgory;
import org.morib.server.api.modalView.vo.TaskInfoInAllowedSite;
import org.morib.server.domain.allowedSite.application.CreateAllowedSiteService;
import org.morib.server.domain.allowedSite.application.FetchAllowedSiteService;
import org.morib.server.domain.allowedSite.application.FetchTabNameService;
import org.morib.server.domain.allowedSite.infra.AllowedSite;
import org.morib.server.domain.allowedSite.infra.type.OwnerType;
import org.morib.server.domain.category.application.CreateCategoryService;
import org.morib.server.domain.category.application.FetchCategoryService;
import org.morib.server.domain.category.infra.Category;
import org.morib.server.domain.task.application.FetchTaskService;
import org.morib.server.domain.task.infra.Task;
import org.morib.server.domain.user.application.FetchUserService;
import org.morib.server.domain.user.infra.User;
import org.springframework.transaction.annotation.Transactional;
import org.morib.server.api.homeView.vo.CategoryInfo;
import org.morib.server.api.modalView.dto.TabNameByUrlResponse;
import org.morib.server.domain.category.application.DeleteCategoryService;
import org.morib.server.domain.category.application.FetchCategoryService;
import org.morib.server.domain.user.application.FetchUserService;
import org.morib.server.domain.user.infra.User;
import org.springframework.stereotype.Service;


import java.util.List;

@RequiredArgsConstructor
@Facade
public class ModalViewFacade {
    private final FetchUserService fetchUserService;
    private final FetchCategoryService fetchCategoryService;
    private final DeleteCategoryService deleteCategoryService;
    private final FetchUserService fetchUserService;
    private final CreateCategoryService createCategoryService;
    private final FetchCategoryService fetchCategoryService;
    private final CreateAllowedSiteService createAllowedSiteService;
    private final FetchAllowedSiteService fetchAllowedSiteService;
    private final FetchTaskService fetchTaskService;
    private final FetchTabNameService fetchTabNameService;

    @Transactional
    public void createCategory(Long userId, CreateCategoryRequestDto createCategoryRequestDto) {
        User user = fetchUserService.fetchByUserId(userId);
        Category createdCategory = createCategoryService.create(createCategoryRequestDto.name(),
            createCategoryRequestDto.startDate(), createCategoryRequestDto.endDate(), user);
        createCategoryRequestDto.allowedSites().stream().map(
            allowedSite -> createAllowedSiteService.create(allowedSite.getSiteName(),
                allowedSite.getSiteUrl(), OwnerType.CATEGORY, createdCategory.getId()));
    }

    @Transactional(readOnly = true)
    public AllowedSiteByCategoryResponseDto fetchAllowedSiteByCategoryId(Long mockUserId,
        Long categoryId) {
        User findUser = fetchUserService.fetchByUserId(mockUserId);
        Category findCategory = fetchCategoryService.fetchByIdAndUser(categoryId, findUser);
        List<AllowedSite> findAllowedSites = fetchAllowedSiteService.fetchByCategoryId(categoryId);
        CategoryInfoInAllowedSite catgoryInfoInAllowedSite = CategoryInfoInAllowedSite.of(categoryId,
            findCategory);
        List<AllowSiteForCalledByCatgory> msetList = mappedByAllowSiteForCalledByCategory(findAllowedSites);

        return AllowedSiteByCategoryResponseDto.of(catgoryInfoInAllowedSite, msetList);
    }

    private List<AllowSiteForCalledByCatgory> mappedByAllowSiteForCalledByCategory(
        List<AllowedSite> allowedSites) {
        return allowedSites.stream().map(AllowSiteForCalledByCatgory::of)
            .toList();
    }

    @Transactional(readOnly = true)
    public AllowedSiteByTaskResponseDto fetchAllowedSiteByTaskId(Long taskId) {
        Task findTask = fetchTaskService.fetchById(taskId);
        List<AllowedSite> findAllowedSites = fetchAllowedSiteService.fetchByTaskId(taskId);
        TaskInfoInAllowedSite taskInfoInAllowedSite = TaskInfoInAllowedSite.of(findTask);
        List<AllowSiteForCalledByTask> msets = mappedByAllowSiteForCalledByTask(findAllowedSites);

        return AllowedSiteByTaskResponseDto.of(taskInfoInAllowedSite, msets);
    }

    private List<AllowSiteForCalledByTask> mappedByAllowSiteForCalledByTask(List<AllowedSite> allowedSites) {
        return allowedSites.stream().
                map(AllowSiteForCalledByTask::of)
                .toList();

    public void deleteCategoryById(Long categoryId) {
        deleteCategoryService.deleteById(categoryId);

    public List<CategoryInfo> fetchCategories(Long userId) {
        User user = fetchUserService.fetchByUserId(userId);
        return fetchCategoryService.fetchByUser(user).stream()
                .map(CategoryInfo::of)
                .toList();
    }

    public TabNameByUrlResponse fetchTabNameByUrl(String url) {
        return TabNameByUrlResponse.of(fetchTabNameService.fetch(url));
    }
}
