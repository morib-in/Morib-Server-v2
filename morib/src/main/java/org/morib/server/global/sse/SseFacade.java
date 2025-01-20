package org.morib.server.global.sse;

import lombok.RequiredArgsConstructor;
import org.morib.server.annotation.Facade;
import org.morib.server.api.homeView.facade.HomeViewFacade;
import org.morib.server.domain.relationship.application.FetchRelationshipService;
import org.morib.server.domain.relationship.infra.Relationship;
import org.morib.server.domain.task.application.FetchTaskService;
import org.morib.server.domain.task.infra.Task;
import org.morib.server.domain.timer.TimerManager;
import org.morib.server.domain.timer.application.FetchTimerService;
import org.morib.server.domain.timer.infra.Timer;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalDate;
import java.util.List;

import static org.morib.server.global.common.Constants.SSE_EVENT_REFRESH;

@Facade
@RequiredArgsConstructor
public class SseFacade {

    private final SseService sseService;
    private final FetchTaskService fetchTaskService;
    private final FetchTimerService fetchTimerService;
    private final FetchRelationshipService fetchRelationshipService;
    private final TimerManager timerManager;
    private final HomeViewFacade homeViewFacade;
    private final SseRepository sseRepository;

    public SseEmitter refresh(Long userId, UserInfoDtoForSseUserInfoWrapper userInfoDtoForSseUserInfoWrapper) {
        // 1. emitter 재생성
        SseEmitter emitter = sseService.create();

        // 2. dto로 전달받은 task의 elapsedTime, taskId, runningCategoryName SseUserInfoWrapper에 세팅
        UserInfoDtoForSseUserInfoWrapper calculatedSseUserInfoWrapper = update(userId, userInfoDtoForSseUserInfoWrapper);

        // 3. repository에 저장
        sseService.saveSseUserInfo(userId, emitter, calculatedSseUserInfoWrapper);

        // 4. broadcast
        List<Relationship> relationships = fetchRelationshipService.fetchConnectedRelationship(userId);
        sseRepository.broadcast(userId, calculatedSseUserInfoWrapper, SSE_EVENT_REFRESH, relationships);

        return emitter;
    }

    public UserInfoDtoForSseUserInfoWrapper updateWhenTimerStart(Long userId, UserInfoDtoForSseUserInfoWrapper wrapper) {
        // 1. Timer DB update (addElapsedTime)
        Task findTask = fetchTaskService.fetchById(wrapper.taskId());
        Timer timer = fetchTimerService.fetchByTaskAndTargetDate(findTask, LocalDate.now());
        timerManager.setElapsedTime(timer, wrapper.elapsedTime());

        // 2. Emitter Value Update (categoryName, elapsedTime, taskId)
        int calculatedElapsedTime = homeViewFacade.fetchTotalElapsedTimeTodayByUser(userId, LocalDate.now()).sumTodayElapsedTime();

        return UserInfoDtoForSseUserInfoWrapper.of(userId, calculatedElapsedTime, wrapper.runningCategoryName(), wrapper.taskId());
    }

    public UserInfoDtoForSseUserInfoWrapper update(Long userId, UserInfoDtoForSseUserInfoWrapper wrapper) {
        // 1. Timer DB update (addElapsedTime)
        Task findTask = fetchTaskService.fetchById(wrapper.taskId());
        Timer timer = fetchTimerService.fetchByTaskAndTargetDate(findTask, LocalDate.now());
        timerManager.addElapsedTime(timer, wrapper.elapsedTime());

        // 2. Emitter Value Update (categoryName, elapsedTime, taskId)
        int calculatedElapsedTime = homeViewFacade.fetchTotalElapsedTimeTodayByUser(userId, LocalDate.now()).sumTodayElapsedTime();

        return UserInfoDtoForSseUserInfoWrapper.of(userId, calculatedElapsedTime, wrapper.runningCategoryName(), wrapper.taskId());
    }


}
