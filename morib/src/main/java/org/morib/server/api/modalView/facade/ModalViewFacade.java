package org.morib.server.api.modalView.facade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.morib.server.annotation.Facade;
import org.morib.server.api.modalView.dto.*;
import org.morib.server.domain.allowedSite.application.FetchTabNameService;
import org.morib.server.domain.category.CategoryManager;
import org.morib.server.domain.category.application.CreateCategoryService;
import org.morib.server.domain.category.application.FetchCategoryService;
import org.morib.server.domain.category.infra.Category;
import org.morib.server.domain.user.application.service.FetchUserService;

import org.morib.server.domain.relationship.application.FetchRelationshipService;
import org.morib.server.domain.relationship.infra.Relationship;
import org.morib.server.domain.user.application.service.FetchUserService;
import org.morib.server.domain.user.infra.User;
import org.morib.server.api.homeView.vo.CategoryInfo;
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
    private final FetchRelationshipService fetchRelationshipService;

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

    public List<FetchRelationshipResponseDto> fetchConnectedRelationships(Long userId) {
        return buildFetchRelationshipResponseDto(userId, fetchRelationshipService.fetchConnectedRelationship(userId));
    }

    public List<FetchRelationshipResponseDto> buildFetchRelationshipResponseDto(Long userId, List<Relationship> relationships) {
        List<User> classifiedRelationships = classifyRelationships(relationships, userId)
                .values()
                .stream()
                .flatMap(List::stream)
                .toList();
        return classifiedRelationships.stream().map(FetchRelationshipResponseDto::of).toList();
    }

    public FetchUnconnectedRelationshipResponseDto fetchUnconnectedRelationships(Long userId) {
        return buildFetchUnconnectedRelationshipResponseDto(userId, fetchRelationshipService.fetchUnconnectedRelationship(userId));
    }

    public FetchUnconnectedRelationshipResponseDto buildFetchUnconnectedRelationshipResponseDto(Long userId, List<Relationship> relationships) {
        Map<String, List<User>> classifiedRelationships = classifyRelationships(relationships, userId);

        return FetchUnconnectedRelationshipResponseDto.of(
                classifiedRelationships.get("send").stream().map(FetchRelationshipResponseDto::of).toList(),
                classifiedRelationships.get("receive").stream().map(FetchRelationshipResponseDto::of).toList());
    }

    public Map<String, List<User>> classifyRelationships(List<Relationship> relationships, Long userId) {
        List<User> send = new ArrayList<>();
        List<User> receive = new ArrayList<>();

        for (Relationship relationship : relationships) {
            if (relationship.getUser().getId().equals(userId)) {
                send.add(relationship.getFriend());
            } else {
                receive.add(relationship.getUser());
            }
        }

        Map<String, List<User>> result = new HashMap<>();
        result.put("send", send);
        result.put("receive", receive);

        return result;
    }

}
