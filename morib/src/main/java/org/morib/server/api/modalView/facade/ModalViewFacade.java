package org.morib.server.api.modalView.facade;

import lombok.RequiredArgsConstructor;
import org.morib.server.annotation.Facade;
import org.morib.server.api.homeView.vo.CategoryInfo;
import org.morib.server.api.modalView.dto.*;
import org.morib.server.domain.allowedSite.application.FetchTabNameService;
import org.morib.server.domain.category.CategoryManager;
import org.morib.server.domain.category.application.CreateCategoryService;
import org.morib.server.domain.category.application.DeleteCategoryService;
import org.morib.server.domain.category.application.FetchCategoryService;
import org.morib.server.domain.category.infra.Category;
import org.morib.server.domain.relationship.RelationshipManager;
import org.morib.server.domain.relationship.application.CreateRelationshipService;
import org.morib.server.domain.relationship.application.DeleteRelationshipService;
import org.morib.server.domain.relationship.application.FetchRelationshipService;
import org.morib.server.domain.relationship.application.ValidateRelationshipService;
import org.morib.server.domain.relationship.infra.Relationship;
import org.morib.server.domain.relationship.infra.type.RelationLevel;
import org.morib.server.domain.timer.application.FetchTimerService;
import org.morib.server.domain.user.application.service.FetchUserService;
import org.morib.server.domain.user.infra.User;
import org.morib.server.global.message.SseMessageBuilder;
import org.morib.server.global.sse.application.repository.SseRepository;
import org.morib.server.global.sse.application.service.SseSender;
import org.morib.server.global.sse.application.service.SseService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.morib.server.global.common.Constants.*;


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
    private final CreateRelationshipService createRelationshipService;
    private final DeleteRelationshipService deleteRelationshipService;
    private final ValidateRelationshipService validateRelationshipService;
    private final RelationshipManager relationshipManager;
    private final FetchTimerService fetchTimerService;
    private final SseSender sseSender;
    private final SseService sseService;
    private final SseMessageBuilder sseMessageBuilder;

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

    @Transactional
    public void createRelationship(Long userId, CreateRelationshipRequestDto createRelationshipRequestDto) {
        User findUser = fetchUserService.fetchByUserId(userId);
        User findFriend = fetchUserService.fetchByUserEmail(createRelationshipRequestDto.friendEmail());
        validateRelationshipService.validateRelationshipByUserAndFriend(findUser, findFriend);
        createRelationshipService.create(findUser, findFriend);
        SseEmitter findFriendEmitter = sseService.fetchSseEmitterByUserId(findFriend.getId());
        if (findFriendEmitter != null) sseSender.sendEvent(findFriendEmitter, SSE_EVENT_FRIEND_REQUEST, sseMessageBuilder.buildFriendRequestMessage(findUser.getName()));
    }

    public List<FetchRelationshipResponseDto> fetchConnectedRelationships(Long userId) {
        return buildFetchRelationshipResponseDto(userId, fetchRelationshipService.fetchConnectedRelationship(userId));
    }

    public List<FetchRelationshipResponseDto> buildFetchRelationshipResponseDto(Long userId, List<Relationship> relationships) {
        return classifyRelationships(relationships, userId).values().stream()
                .flatMap(List::stream)
                .map(user -> FetchRelationshipResponseDto.of(
                        user,
                        sseService.validateConnection(user.getId()),
                        fetchTimerService.sumElapsedTimeByUser(user, LocalDate.now())
                ))
                .toList();
    }

    public FetchUnconnectedRelationshipResponseDto fetchUnconnectedRelationships(Long userId) {
        return buildFetchUnconnectedRelationshipResponseDto(userId, fetchRelationshipService.fetchUnconnectedRelationship(userId));
    }

    public FetchUnconnectedRelationshipResponseDto buildFetchUnconnectedRelationshipResponseDto(Long userId, List<Relationship> relationships) {
        Map<String, List<User>> classifiedRelationships = classifyRelationships(relationships, userId);

        return FetchUnconnectedRelationshipResponseDto.of(
                classifiedRelationships.get(SEND).stream().map(FetchRelationshipRequestsResponseDto::of).toList(),
                classifiedRelationships.get(RECEIVE).stream().map(FetchRelationshipRequestsResponseDto::of).toList());
    }

    public Map<String, List<User>> classifyRelationships(List<Relationship> relationships, Long userId) {
        List<User> send = new ArrayList<>();
        List<User> receive = new ArrayList<>();
        for (Relationship relationship : relationships) {
            if (relationship.getUser().getId().equals(userId)) send.add(relationship.getFriend());
            else receive.add(relationship.getUser());
        }

        Map<String, List<User>> result = new HashMap<>();
        result.put(SEND, send);
        result.put(RECEIVE, receive);
        return result;
    }

    @Transactional
    public void acceptPendingFriendRequest(Long userId, Long friendId) {
        Relationship relationship = fetchRelationshipService.fetchRelationshipByUserIdAndFriendId(friendId, userId, RelationLevel.UNCONNECTED);
        relationshipManager.updateRelationLevelToConnect(relationship);
        User friend = fetchUserService.fetchByUserId(friendId);
        SseEmitter findFriendEmitter = sseService.fetchSseEmitterByUserId(friendId);
        if (findFriendEmitter != null) sseSender.sendEvent(findFriendEmitter, SSE_EVENT_FRIEND_REQUEST_ACCEPT, sseMessageBuilder.buildFriendRequestAcceptMessage(friend.getName()));
    }

    @Transactional
    public void cancelPendingFriendRequest(Long userId, Long friendId) {
        Relationship relationship = fetchRelationshipService.fetchRelationshipByUserIdAndFriendId(userId, friendId, RelationLevel.UNCONNECTED);
        deleteRelationshipService.delete(relationship);
    }

    @Transactional
    public void rejectPendingFriendRequest(Long userId, Long friendId) {
        Relationship relationship = fetchRelationshipService.fetchRelationshipByUserIdAndFriendId(friendId, userId, RelationLevel.UNCONNECTED);
        deleteRelationshipService.delete(relationship);
    }

    @Transactional
    public void deleteFriend(Long userId, Long friendId) {
        Relationship relationship = fetchRelationshipService.fetchRelationshipByUserIdAndFriendId(friendId, userId, RelationLevel.CONNECTED);
        deleteRelationshipService.delete(relationship);
    }
}
