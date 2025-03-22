package org.morib.server.global.sse.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.morib.server.annotation.Facade;
import org.morib.server.api.homeView.facade.HomeViewFacade;
import org.morib.server.domain.category.application.FetchCategoryService;
import org.morib.server.domain.category.infra.Category;
import org.morib.server.domain.relationship.application.FetchRelationshipService;
import org.morib.server.domain.task.application.FetchTaskService;
import org.morib.server.domain.timer.TimerManager;
import org.morib.server.domain.timer.application.FetchTimerService;
import org.morib.server.domain.timer.infra.TimerStatus;
import org.morib.server.global.exception.SSEConnectionException;
import org.morib.server.global.message.ErrorMessage;
import org.morib.server.global.message.SseMessageBuilder;
import org.morib.server.global.sse.application.event.SseDisconnectEvent;
import org.morib.server.global.sse.application.repository.SseRepository;
import org.morib.server.global.sse.application.repository.SseUserInfoWrapper;
import org.morib.server.global.sse.application.service.SseSender;
import org.morib.server.global.sse.application.service.SseService;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;

import static org.morib.server.global.common.Constants.SSE_EVENT_CONNECT;

@Facade
@RequiredArgsConstructor
@Slf4j
public class SseFacade {

    private final SseService sseService;
    private final FetchTimerService fetchTimerService;
    private final FetchCategoryService fetchCategoryService;
    private final FetchRelationshipService fetchRelationshipService;
    private final TimerManager timerManager;
    private final SseSender sseSender;
    private final SseMessageBuilder sseMessageBuilder;

    public SseEmitter init(Long userId) {
        try {
            // 기존 연결이 있으면 종료
            sseService.removeExistingEmitter(userId);
            SseEmitter createdEmitter = sseService.create();
            sseService.add(userId, createdEmitter);
//            for (int i=0; i<10; i++) {
//                if (!sseService.validateConnection(userId)) {
//                    // emitter 생성 후 저장
//                    sseService.add(userId, createdEmitter);
//                    break;
//                }
//                else {
//                    log.info("SseEmitter is already connected. Retry to connect ... count : {}", i);
//                    Thread.sleep(10);
//                }
//            }
            // 브로드캐스트
            return broadcastAllAfterCreated(userId, createdEmitter, SSE_EVENT_CONNECT);
        } catch (Exception e) {
            log.error("SSE 초기화 중 오류 발생: {}", e.getMessage(), e);
            throw new SSEConnectionException(ErrorMessage.SSE_CONNECT_FAILED);
        }
    }

    public SseUserInfoWrapper refresh(Long userId, int elapsedTime, Long taskId, TimerStatus timerStatus) {
        if (elapsedTime == 0 || taskId == null) {
            log.info("refresh 요청 시, elapsedTime 또는 taskId가 null입니다. userInfos를 업데이트하지 않습니다.");
            return null;
        }
        // 전달받은 내용을 userInfos에 업데이트
        Category findCategory = fetchCategoryService.fetchByUserIdAndTaskId(userId, taskId);
        SseUserInfoWrapper updatedSseUserInfoWrapper = updateAndBuildSseUserInfoWrapperWhenRefreshOrRunTimer(userId, taskId, elapsedTime, findCategory.getName(), timerStatus);
        sseService.saveSseUserInfoWrapper(userId, updatedSseUserInfoWrapper);
        return updatedSseUserInfoWrapper;
    }

    public SseEmitter broadcastAllAfterCreated(Long userId, SseEmitter createdEmitter, String sseEventMessage) {
        sseSender.sendEvent(createdEmitter, sseEventMessage, sseMessageBuilder.buildConnectionMessage(userId));
        List<Long> targetUserIds = fetchRelationshipService.fetchConnectedRelationshipAndClassify(userId);
        List<SseEmitter> targetEmitters = sseService.fetchConnectedSseEmittersById(targetUserIds);
        sseSender.broadcast(targetEmitters, sseEventMessage, sseMessageBuilder.buildConnectionMessage(userId));
        return createdEmitter;
    }

    // refresh 시에는 현재 실행중인 타이머 정보를 보내므로 elapsedTime을 sse userInfos에 그대로 저장 (timer.setElapsedTime)
    public SseUserInfoWrapper updateAndBuildSseUserInfoWrapperWhenRefreshOrRunTimer(Long userId, Long taskId, int elapsedTime, String runningCategoryName, TimerStatus timerStatus) {
        Category findCategory = fetchCategoryService.fetchByUserIdAndTaskId(userId, taskId);
        findCategory.getTasks().stream()
                .filter(task -> task.getId().equals(taskId))
                .findFirst()
                .flatMap(task -> task.getTimers().stream()
                        .filter(timer -> timer.getTargetDate().equals(LocalDate.now()))
                        .findFirst()).ifPresent(timer -> timerManager.setElapsedTime(timer, elapsedTime));
        return SseUserInfoWrapper.of(elapsedTime, runningCategoryName, taskId, timerStatus, LocalDateTime.now());
    }

    public SseUserInfoWrapper updateAndBuildSseUserInfoWrapperWhenStopTimer(Long userId, Long taskId, int elapsedTime, String runningCategoryName, TimerStatus timerStatus) {
        Category findCategory = fetchCategoryService.fetchByUserIdAndTaskId(userId, taskId);
        findCategory.getTasks().stream()
                .filter(task -> task.getId().equals(taskId))
                .findFirst()
                .flatMap(task -> task.getTimers().stream()
                        .filter(timer -> timer.getTargetDate().equals(LocalDate.now()))
                        .findFirst()).ifPresent(timer -> timerManager.addElapsedTime(timer, elapsedTime)
                );
        int elapsedTimeAfterAddOperation = fetchTimerService.fetchByTaskIdAndTargetDate(taskId, LocalDate.now()).getElapsedTime();
        return SseUserInfoWrapper.of(elapsedTimeAfterAddOperation, runningCategoryName, taskId, timerStatus, LocalDateTime.now());
    }
}
