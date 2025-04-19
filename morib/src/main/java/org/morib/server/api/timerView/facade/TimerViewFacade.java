package org.morib.server.api.timerView.facade;

import lombok.RequiredArgsConstructor;
import org.morib.server.annotation.Facade;
import org.morib.server.api.allowGroupView.dto.AllowedSiteWithIdVo;
import org.morib.server.api.allowGroupView.facade.AllowedGroupViewFacade;
import org.morib.server.api.modalView.dto.FetchRelationshipResponseDto;
import org.morib.server.api.modalView.facade.ModalViewFacade;
import org.morib.server.api.timerView.dto.*;
import org.morib.server.domain.allowedGroup.application.FetchAllowedGroupService;
import org.morib.server.domain.allowedGroup.infra.AllowedGroup;
import org.morib.server.domain.category.application.FetchCategoryService;
import org.morib.server.domain.category.infra.Category;
import org.morib.server.domain.recentAllowedGroup.application.CreateRecentAllowedGroupService;
import org.morib.server.domain.recentAllowedGroup.application.DeleteRecentAllowedGroupService;
import org.morib.server.domain.recentAllowedGroup.application.FetchRecentAllowedGroupService;
import org.morib.server.domain.recentAllowedGroup.infra.RecentAllowedGroup;
import org.morib.server.domain.relationship.application.FetchRelationshipService;
import org.morib.server.domain.relationship.infra.Relationship;
import org.morib.server.domain.task.application.FetchTaskService;
import org.morib.server.domain.task.infra.Task;
import org.morib.server.domain.timer.TimerManager;
import org.morib.server.domain.timer.TimerSessionManager;
import org.morib.server.domain.timer.application.FetchTimerService;
import org.morib.server.domain.timer.application.TimerSession.CreateTimerSessionService;
import org.morib.server.domain.timer.application.TimerSession.FetchTimerSessionService;
import org.morib.server.domain.timer.infra.Timer;
import org.morib.server.domain.timer.infra.TimerSession;
import org.morib.server.domain.timer.infra.TimerStatus;
import org.morib.server.domain.todo.application.FetchTodoService;
import org.morib.server.domain.todo.infra.Todo;
import org.morib.server.domain.user.application.service.FetchUserService;
import org.morib.server.domain.user.infra.User;
import org.morib.server.global.common.HealthCheckController;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Facade
@RequiredArgsConstructor
public class TimerViewFacade {

    private final FetchTimerService fetchTimerService;
    private final FetchTaskService fetchTaskService;
    private final FetchTodoService fetchTodoService;
    private final FetchUserService fetchUserService;
    private final FetchRelationshipService fetchRelationshipService;
    private final ModalViewFacade modalViewFacade;
    private final AllowedGroupViewFacade allowedGroupViewFacade;
    private final FetchRecentAllowedGroupService fetchRecentAllowedGroupService;
    private final DeleteRecentAllowedGroupService deleteRecentAllowedGroupService;
    private final CreateRecentAllowedGroupService createRecentAllowedGroupService;
    private final FetchAllowedGroupService fetchAllowedGroupService;
    private final FetchTimerSessionService fetchTimerSessionService;
    private final CreateTimerSessionService createTimerSessionService;
    private final FetchCategoryService fetchCategoryService;
    private final TimerSessionManager timerSessionManager;
    private final HealthCheckController healthCheckController;
    private final TimerManager timerManager;
    @Transactional
    public void runTimer(Long userId, TimerDtos.TimerRequest requestDto) {
        LocalDateTime now = LocalDateTime.now();
        TimerSession findTimerSession = fetchTimerSessionService.fetchTimerSession(userId, requestDto.targetDate());
        if (findTimerSession == null || !Objects.equals(findTimerSession.getSelectedTask().getId(), requestDto.taskId())) {
            throw new NotFoundException(ErrorMessage.TIMER_SESSION_NOT_FOUND);
        }
        timerSessionManager.run(findTimerSession, now);
    }

    @Transactional
    public void pauseTimer(Long userId, TimerDtos.TimerRequest requestDto) {
        LocalDateTime now = LocalDateTime.now();
        TimerSession findTimerSession = fetchTimerSessionService.fetchTimerSession(userId, requestDto.targetDate());
        if (findTimerSession == null || !Objects.equals(findTimerSession.getSelectedTask().getId(), requestDto.taskId())) {
            throw new NotFoundException(ErrorMessage.TIMER_SESSION_NOT_FOUND);
        }
        if (findTimerSession.getTimerStatus().equals(TimerStatus.PAUSED)) return;

        int calculatedElapsedTime = calculateTimerSessionService.calculateElapsedTimeByLastCalculatedAt(findTimerSession, LocalDateTime.now());
        timerSessionManager.pause(calculatedElapsedTime, findTimerSession, now);
        timerManager.setElapsedTime(fetchTimerService.fetchByTaskIdAndTargetDate(requestDto.taskId(), requestDto.targetDate()), findTimerSession.getElapsedTime());
    }

    @Transactional
    public TimerDtos.TimerStatusResponse getSelectedTimerInfo(Long userId, LocalDate targetDate) {
        LocalDateTime now = LocalDateTime.now();
        TimerSession findTimerSession = fetchTimerSessionService.fetchTimerSession(userId, targetDate);
        if (findTimerSession == null) throw new NotFoundException(ErrorMessage.TIMER_SESSION_NOT_FOUND);
        if (findTimerSession.getTimerStatus().equals(TimerStatus.RUNNING)) {
            int calculatedElapsedTime = calculateTimerSessionService.calculateElapsedTimeByLastCalculatedAt(findTimerSession, LocalDateTime.now());
            findTimerSession = timerSessionManager.handleCalledByClientFetch(calculatedElapsedTime, findTimerSession, now);
            timerManager.setElapsedTime(fetchTimerService.fetchByTaskIdAndTargetDate(findTimerSession.getSelectedTask().getId(), findTimerSession.getTargetDate()), findTimerSession.getElapsedTime());
        }
        return TimerDtos.TimerStatusResponse.from(findTimerSession);
    }

    @Transactional
    public void selectTimerInfo(Long userId, TimerDtos.TimerRequest requestDto) {
        User user = fetchUserService.fetchByUserId(userId);
        Category findCategory = fetchCategoryService.fetchByUserIdAndTaskId(userId, requestDto.taskId());
        Task selectedTask = fetchTaskService.fetchById(requestDto.taskId());
        Timer timer = fetchTimerService.fetchOrCreateByTaskAndTargetDate(user, selectedTask, requestDto.targetDate());
        TimerSession findTimerSession = fetchTimerSessionService.fetchTimerSession(userId, requestDto.targetDate());
        if (findTimerSession == null) throw new NotFoundException(ErrorMessage.TIMER_SESSION_NOT_FOUND);
        timerSessionManager.updateTimerSession(findTimerSession, findCategory.getName(), selectedTask, timer.getElapsedTime(), TimerStatus.PAUSED, requestDto.targetDate());
    }

    @Transactional
    public void handleHeartbeat(Long userId, LocalDate targetDate) {
        LocalDateTime now = LocalDateTime.now();
        TimerSession findTimerSession = fetchTimerSessionService.fetchTimerSession(userId, targetDate);
        timerSessionManager.handleHeartbeat(findTimerSession, now);
    }

    @Transactional
    public TodoCardResponseDto fetchTodoCard(Long userId, LocalDate targetDate) {
        int totalTimeOfToday = fetchTimerService.sumElapsedTimeByUser(fetchUserService.fetchByUserId(userId), targetDate);
        Optional<Todo> todo = fetchTodoService.fetchByUserIdAndTargetDate(userId, targetDate);

        if (todo.isEmpty()) return new TodoCardResponseDto(totalTimeOfToday, Collections.emptyList());

        LinkedHashSet<Task> tasks = fetchTaskService.fetchByTodoAndSameTargetDate(todo.get(), targetDate);

        List<TaskInTodoCardDto> taskInTodoCardDtos = tasks.stream()
            .map(t -> getTaskInTodoCardDto(targetDate, t))
            .toList();
        return new TodoCardResponseDto(totalTimeOfToday, taskInTodoCardDtos);
    }

    private TaskInTodoCardDto getTaskInTodoCardDto(LocalDate targetDate, Task task) {
        return TaskInTodoCardDto.of(task, targetDate, fetchTimerService.sumOneTaskElapsedTimeInTargetDate(task, targetDate));
    }

    public List<FriendsInTimerResponseDto> fetchFriendsInfo(Long userId) {
        List<FetchRelationshipResponseDto> fetchRelationshipResponseDtoList = fetchConnectedRelationships(userId);
        return fetchRelationshipResponseDtoList.stream()
                .map(dto -> {
                    if (fetchTimerSessionService.fetchTimerSession(dto.id(), LocalDate.now()) == null) {
                      return FriendsInTimerResponseDto.of(dto, 0, "", TimerStatus.PAUSED);
                    }
                    else {
                        TimerSession timerSession = fetchTimerSessionService.fetchTimerSession(dto.id(), LocalDate.now());
                        return FriendsInTimerResponseDto.of(dto, dto.elapsedTime(), timerSession.getRunningCategoryName(), timerSession.getTimerStatus());
                    }
                }).toList();
    }

    public List<FetchRelationshipResponseDto> fetchConnectedRelationships(Long userId) {
        return buildFetchRelationshipResponseDto(userId, fetchRelationshipService.fetchConnectedRelationship(userId));
    }

    public List<FetchRelationshipResponseDto> buildFetchRelationshipResponseDto(Long userId, List<Relationship> relationships) {
        return modalViewFacade.classifyRelationships(relationships, userId).values().stream()
                .flatMap(List::stream)
                .filter(user -> fetchTimerService.sumElapsedTimeByUser(user, LocalDate.now()) > 0)
                .map(user -> FetchRelationshipResponseDto.of(user, healthCheckController.isUserActive(user.getId())))
                .sorted(Comparator.comparing(FetchRelationshipResponseDto::isOnline).reversed()
                        .thenComparing(FetchRelationshipResponseDto::elapsedTime).reversed()
                        .thenComparing(FetchRelationshipResponseDto::name))
                .toList();
    }

    public void assignAllowedGroupsInTimer(Long userId, AssignAllowedGroupsRequestDto assignAllowedGroupsRequestDto) {
        List<Long> currentIdList = fetchRecentAllowedGroupService.findAllByUserId(userId).stream().map(recentAllowedGroup -> recentAllowedGroup.getSelectedAllowedGroup().getId()).toList();
        List<Long> targetIdList = assignAllowedGroupsRequestDto.allowedGroupIdList();

        List<Long> toDelete = currentIdList.stream().filter(i -> !targetIdList.contains(i)).toList();
        List<Long> toInsert = targetIdList.stream().filter(i -> !currentIdList.contains(i)).toList();

        toDelete.forEach(deleteRecentAllowedGroupService::deleteByAllowedGroupId);
        createRecentAllowedGroupService.create(buildRecentAllowedGroups(userId, toInsert));
    }

    public List<RecentAllowedGroup> buildRecentAllowedGroups(Long userId, List<Long> allowedGroupIdList) {
        User user = fetchUserService.fetchByUserId(userId);
        List<AllowedGroup> allowedGroupList = allowedGroupIdList.stream().map(fetchAllowedGroupService::findById).toList();
        return allowedGroupList.stream().map(
                allowedGroup -> RecentAllowedGroup.create(user, allowedGroup)).toList();
    }

    @Transactional(readOnly = true)
    public List<FetchAllowedGroupInTimerResponseDto> fetchAllowedGroupsInTimer(Long userId) {
        List<AllowedGroup> findAllowedGroups = fetchAllowedGroupService.findAllByUserId(userId);
        Set<Long> findRecentAllowedGroupIdSet = fetchRecentAllowedGroupService.findAllByUserId(userId).stream().map(recentAllowedGroup -> recentAllowedGroup.getSelectedAllowedGroup().getId()).collect(Collectors.toUnmodifiableSet());

        return findAllowedGroups.stream().map(
                allowedGroup -> {
                    List<AllowedSiteWithIdVo> allowedGroupDetailAllowedSiteVos = allowedGroupViewFacade.filterByTopDomainAndGetAllowedSiteWithIdVo(allowedGroup);
                    return FetchAllowedGroupInTimerResponseDto.of(allowedGroup.getId(),
                            allowedGroup.getName(), allowedGroup.getColorCode(), findRecentAllowedGroupIdSet.contains(allowedGroup.getId()), allowedGroupDetailAllowedSiteVos);
                }).toList();

    }

}
