package org.morib.server.api.modalView.facade;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.morib.server.annotation.Facade;
import org.morib.server.api.modalView.dto.CreateCategoryRequestDto;
import org.morib.server.api.modalView.dto.UpdateCategoryNameRequestDto;
import org.morib.server.api.modalView.dto.FetchFriendsResponseDto;
import org.morib.server.domain.allowedSite.application.FetchTabNameService;
import org.morib.server.domain.category.CategoryManager;
import org.morib.server.domain.category.application.CreateCategoryService;
import org.morib.server.domain.category.application.FetchCategoryService;
import org.morib.server.domain.category.infra.Category;
import org.morib.server.domain.relationship.application.FetchFriendsService;
import org.morib.server.domain.relationship.infra.Relationship;
import org.morib.server.domain.user.application.FetchUserService;
import org.morib.server.domain.user.infra.User;
import org.morib.server.api.homeView.vo.CategoryInfo;
import org.morib.server.api.modalView.dto.TabNameByUrlResponse;
import org.morib.server.domain.category.application.DeleteCategoryService;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Facade
public class ModalViewFacade {
    private final FetchUserService fetchUserService;
    private final FetchCategoryService fetchCategoryService;
    private final DeleteCategoryService deleteCategoryService;
    private final CreateCategoryService createCategoryService;
    private final FetchTabNameService fetchTabNameService;
    private final CategoryManager categoryManager;
    private final FetchFriendsService fetchFriendsService;

    @Transactional
    public void createCategory(Long userId, CreateCategoryRequestDto createCategoryRequestDto) {
        User user = fetchUserService.fetchByUserId(userId);
        createCategoryService.create(createCategoryRequestDto.name(), user);
    }

    @Transactional
    public void deleteCategoryById(Long categoryId){
            deleteCategoryService.deleteById(categoryId);
    }

    public List<CategoryInfo> fetchCategories(Long userId) {
        User user = fetchUserService.fetchByUserId(userId);
        return fetchCategoryService.fetchByUser(user).stream()
                .map(CategoryInfo::of)
                .toList();
    }

    public TabNameByUrlResponse fetchTabNameByUrl(String url) {
        return TabNameByUrlResponse.of(fetchTabNameService.fetch(url));
    }

    @Transactional
    public void updateCategoryNameById(Long userId, Long categoryId, UpdateCategoryNameRequestDto updateCategoryNameRequestDto) {
        User findUser = fetchUserService.fetchByUserId(userId);
        Category findCategory = fetchCategoryService.fetchByUserAndCategoryId(findUser, categoryId);
        categoryManager.updateName(findCategory, updateCategoryNameRequestDto.name());
    }

    public List<FetchFriendsResponseDto> fetchFriends(Long userId) {
        return fetchFriendsByRelationships(userId, fetchFriendsService.fetch(userId));
    }

    public List<FetchFriendsResponseDto> fetchFriendsByRelationships(Long userId, List<Relationship> relationships) {
        List<User> friends = new ArrayList<>();
        for (Relationship relationship : relationships) {
            if (relationship.getUser().getId().equals(userId)) friends.add(relationship.getFriend());
            else friends.add(relationship.getUser());
        }
        return buildFetchFriendsResponseDto(friends);
    }

    public List<FetchFriendsResponseDto> buildFetchFriendsResponseDto(List<User> friends) {
        return friends.stream().map(FetchFriendsResponseDto::of).toList();
    }

}
