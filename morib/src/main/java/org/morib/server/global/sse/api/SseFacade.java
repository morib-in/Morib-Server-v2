package org.morib.server.global.sse.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.morib.server.annotation.Facade;
import org.morib.server.api.homeView.facade.HomeViewFacade;
import org.morib.server.domain.relationship.application.FetchRelationshipService;
import org.morib.server.domain.task.application.FetchTaskService;
import org.morib.server.domain.task.infra.Task;
import org.morib.server.domain.timer.TimerManager;
import org.morib.server.domain.timer.application.FetchTimerService;
import org.morib.server.domain.timer.infra.Timer;
import org.morib.server.global.exception.SSEConnectionException;
import org.morib.server.global.message.ErrorMessage;
import org.morib.server.global.message.SseMessageBuilder;
import org.morib.server.global.sse.application.service.SseSender;
import org.morib.server.global.sse.application.service.SseService;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalDate;
import java.util.List;

import static org.morib.server.global.common.Constants.SSE_EVENT_CONNECT;
import static org.morib.server.global.common.Constants.SSE_EVENT_REFRESH;

@Facade
@RequiredArgsConstructor
@Slf4j
public class SseFacade {

    private final SseService sseService;
    private final FetchTaskService fetchTaskService;
    private final FetchTimerService fetchTimerService;
    private final FetchRelationshipService fetchRelationshipService;
    private final TimerManager timerManager;
    private final HomeViewFacade homeViewFacade;
    private final SseSender sseSender;
    private final SseMessageBuilder sseMessageBuilder;

    public SseEmitter init(Long userId) {
        try {
            SseEmitter createdEmitter = sseService.create();
            sseService.add(userId, createdEmitter);
            return broadcastAllAfterCreated(userId, createdEmitter, SSE_EVENT_CONNECT);
        } catch (Exception e) {
            log.error("SSE 초기화 중 오류 발생: {}", e.getMessage(), e);
            throw new SSEConnectionException(ErrorMessage.SSE_CONNECT_FAILED);
        }
    }
    public SseEmitter refresh(Long userId, UserInfoDtoForSseUserInfoWrapper userInfoDtoForSseUserInfoWrapper) {
        SseEmitter createdEmitter = sseService.create();
        if (userInfoDtoForSseUserInfoWrapper.taskId() != null) {
            UserInfoDtoForSseUserInfoWrapper calculatedSseUserInfoWrapper = update(userId, userInfoDtoForSseUserInfoWrapper);
            sseService.saveSseUserInfo(userId, createdEmitter, calculatedSseUserInfoWrapper);
        }
        return broadcastAllAfterCreated(userId, createdEmitter, SSE_EVENT_REFRESH);
    }

    public SseEmitter broadcastAllAfterCreated(Long userId, SseEmitter createdEmitter, String sseEventMessage) {
        sseSender.sendEvent(createdEmitter, sseEventMessage, sseMessageBuilder.buildConnectionMessage(userId));
        List<Long> targetUserIds = fetchRelationshipService.fetchConnectedRelationshipAndClassify(userId);
        List<SseEmitter> targetEmitters = sseService.fetchConnectedSseEmittersById(targetUserIds);
        sseSender.broadcast(targetEmitters, sseEventMessage, sseMessageBuilder.buildConnectionMessage(userId));
        return createdEmitter;
    }

    public UserInfoDtoForSseUserInfoWrapper update(Long userId, UserInfoDtoForSseUserInfoWrapper wrapper) {
        Task findTask = fetchTaskService.fetchByIdAndTimer(wrapper.taskId());
        Timer timer = fetchTimerService.fetchByTaskAndTargetDate(findTask, LocalDate.now());
        timerManager.addElapsedTime(timer, wrapper.elapsedTime());
        int calculatedElapsedTime = homeViewFacade.fetchTotalElapsedTimeTodayByUser(userId, LocalDate.now()).sumTodayElapsedTime();
        return UserInfoDtoForSseUserInfoWrapper.of(userId, calculatedElapsedTime, wrapper.runningCategoryName(), wrapper.taskId());
    }
}
