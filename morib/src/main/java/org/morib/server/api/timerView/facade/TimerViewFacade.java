package org.morib.server.api.timerView.facade;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.morib.server.annotation.Facade;
import org.morib.server.api.modalView.dto.FetchRelationshipResponseDto;
import org.morib.server.api.modalView.facade.ModalViewFacade;
import org.morib.server.api.timerView.dto.*;
import org.morib.server.domain.category.application.FetchCategoryService;
import org.morib.server.domain.category.infra.Category;
import org.morib.server.domain.relationship.application.FetchRelationshipService;
import org.morib.server.domain.relationship.infra.Relationship;
import org.morib.server.domain.task.application.FetchTaskService;
import org.morib.server.domain.task.infra.Task;
import org.morib.server.domain.timer.TimerManager;
import org.morib.server.domain.timer.application.FetchTimerService;
import org.morib.server.domain.timer.infra.Timer;
import org.morib.server.domain.todo.application.FetchTodoService;
import org.morib.server.domain.todo.infra.Todo;
import org.morib.server.domain.user.application.service.FetchUserService;
import org.morib.server.domain.user.infra.User;
import org.morib.server.global.sse.SseFacade;
import org.morib.server.global.sse.SseService;
import org.morib.server.global.sse.UserInfoDtoForSseUserInfoWrapper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import static org.morib.server.global.common.Constants.SSE_EVENT_REFRESH;
import static org.morib.server.global.common.Constants.SSE_EVENT_USER_INFO_UPDATE;


@Facade
@RequiredArgsConstructor
public class TimerViewFacade {

    private final FetchTimerService fetchTimerService;
    private final FetchTaskService fetchTaskService;
    private final FetchTodoService fetchTodoService;
    private final FetchUserService fetchUserService;
    private final TimerManager timerManager;
    private final FetchRelationshipService fetchRelationshipService;
    private final SseService sseService;
    private final ModalViewFacade modalViewFacade;
    private final SseFacade sseFacade;

    public void runTimer(Long userId, RunTimerRequestDto runTimerRequestDto) {
        UserInfoDtoForSseUserInfoWrapper calculatedSseUserInfoWrapper = sseFacade.updateWhenTimerStart(userId, UserInfoDtoForSseUserInfoWrapper.of(userId, runTimerRequestDto.elapsedTime(), runTimerRequestDto.runningCategoryName(), runTimerRequestDto.taskId()));
        SseEmitter emitter = sseService.fetchSseEmitterByUserId(userId);
        sseService.saveSseUserInfo(userId, emitter, calculatedSseUserInfoWrapper);
        List<Relationship> relationships = fetchRelationshipService.fetchConnectedRelationship(userId);
        sseService.broadcast(userId, calculatedSseUserInfoWrapper, SSE_EVENT_USER_INFO_UPDATE, relationships);
    }

    public void stopAfterSumElapsedTime(Long userId, Long taskId, StopTimerRequestDto dto) {
        Task findTask = fetchTaskService.fetchById(taskId);
        Timer timer = fetchTimerService.fetchByTaskAndTargetDate(findTask, dto.targetDate());
        timerManager.addElapsedTime(timer, dto.elapsedTime());
        UserInfoDtoForSseUserInfoWrapper calculatedSseUserInfoWrapper = sseFacade.update(
                userId,
                UserInfoDtoForSseUserInfoWrapper.of(
                        userId,
                        dto.elapsedTime(),
                        fetchTaskService.fetchById(taskId).getCategory().getName(),
                        taskId));
        SseEmitter emitter = sseService.fetchSseEmitterByUserId(userId);
        sseService.saveSseUserInfo(userId, emitter, calculatedSseUserInfoWrapper);
        List<Relationship> relationships = fetchRelationshipService.fetchConnectedRelationship(userId);
        sseService.broadcast(userId, calculatedSseUserInfoWrapper, SSE_EVENT_USER_INFO_UPDATE, relationships);
    }

    /**
     *
     * 타이머 뷰 안에서 todo 내의 task 들을 가져온다.
     * 해당 타이머들의 총 시간을 계산해야함!
     * 1. todo를 유저를 통해 찾는다.
     * 2. 찾은 todo 에서 task들을 불러온다.
     * 3. totalTimeToday~ 는 유저id랑, targetDate를 바탕으로 user의 모든 timer를 조회해서 elapsedTime을 더한 값이다.
     * @param targetDate
     * @return
     */
    @Transactional
    public TodoCardResponseDto fetchTodoCard(Long userId, LocalDate targetDate) {
        Todo todo = fetchTodoService.fetchByUserIdAndTargetDate(userId, targetDate);
        LinkedHashSet<Task> tasks = fetchTaskService.fetchByTodoAndSameTargetDate(todo, targetDate);
        int totalTimeOfToday = fetchTimerService.sumElapsedTimeByUser(fetchUserService.fetchByUserId(userId), targetDate);
        List<TaskInTodoCardDto> taskInTodoCardDtos = tasks.stream()
            .map(t -> getTaskInTodoCardDto(targetDate, t))
            .toList();

        return new TodoCardResponseDto(totalTimeOfToday, taskInTodoCardDtos);
    }

    private TaskInTodoCardDto getTaskInTodoCardDto(LocalDate targetDate, Task task) {
        return TaskInTodoCardDto.of(task, targetDate, fetchTimerService.sumOneTaskElapsedTimeInTargetDate(task, targetDate));
    }

    /**
     * 타이머 하단 친구 정보 불러오기
     * 0. 해당 시점의 다른 친구들 정보를 최신화 하기 위한 API 필요할 듯 -> 이는 Timeout 줄여서 간극 줄입시다.
     * 1. 친구 목록 조회 (온, 오프라인 모두)
     * 2. Emitter Repository 에서 실행중인 categoryName 받아와서 FriendsInTimerResponseDto Build
     */
    public List<FriendsInTimerResponseDto> fetchFriendsInfo(Long userId) {
        List<FetchRelationshipResponseDto> fetchRelationshipResponseDtos = modalViewFacade.fetchConnectedRelationships(userId);
        List<FriendsInTimerResponseDto> friendsInTimerResponseDtos = fetchRelationshipResponseDtos.stream().map(dto ->
                FriendsInTimerResponseDto.of(
                        dto,
                        sseService.fetchFriendsRunningCategoryNameBySseEmitters(dto.id())
                )
        ).collect(Collectors.toList()); // mutable list
        friendsInTimerResponseDtos.sort(Comparator.comparing(FriendsInTimerResponseDto::name));
        return friendsInTimerResponseDtos;
    }

}
