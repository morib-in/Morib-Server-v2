package org.morib.server.global.sse;

import lombok.RequiredArgsConstructor;
import org.morib.server.domain.relationship.application.FetchRelationshipService;
import org.morib.server.domain.relationship.infra.Relationship;
import org.morib.server.global.exception.SSEConnectionException;
import org.morib.server.global.message.ErrorMessage;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.morib.server.global.common.Constants.SSE_EVENT_CONNECT;

@Service
@RequiredArgsConstructor
public class SseService {

    private final SseRepository sseRepository;
    private final FetchRelationshipService fetchRelationshipService;

    public SseEmitter create() {
        return sseRepository.create();
    }

    public SseEmitter init(Long userId) {
        SseEmitter emitter = sseRepository.create();
        sseRepository.add(userId, emitter, 0, "", null);
        try {
            emitter.send(SseEmitter.event()
                    .name(SSE_EVENT_CONNECT)
                    .data(("[id : " + userId + "] 가 연결되었습니다.")));
        } catch (IOException e) {
            throw new SSEConnectionException(ErrorMessage.SSE_CONNECT_FAILED);
        }
        List<Relationship> relationships = fetchRelationshipService.fetchConnectedRelationship(userId);
        sseRepository.broadcast(userId, "[id : " + userId + "] 가 연결되었습니다.", SSE_EVENT_CONNECT, relationships);
        return emitter;
    }

    public void saveSseUserInfo(Long userId, SseEmitter emitter, UserInfoDtoForSseUserInfoWrapper calculatedSseUserInfoWrapper) {
        sseRepository.add(
                userId,
                emitter,
                calculatedSseUserInfoWrapper.elapsedTime(),
                calculatedSseUserInfoWrapper.runningCategoryName(),
                calculatedSseUserInfoWrapper.taskId());
    }

    public SseEmitter fetchSseEmitterByUserId(Long userId) {
        return Optional.ofNullable(sseRepository.emitters.get(userId).getSseEmitter()).orElseThrow(
                () -> new SSEConnectionException(ErrorMessage.SSE_CONNECT_FAILED)
        );
    }

    // SseEmitter hashMap에 들어있는 친구들의 카테고리 이름 조회 (온라인인 친구만 조회됨)
    public String fetchFriendsRunningCategoryNameBySseEmitters(Long userId) {
        try {
            if (SseRepository.emitters.containsKey(userId)) {
                return SseRepository.emitters.get(userId).getRunningCategoryName();
            }
        } catch (SSEConnectionException e) {
            throw new SSEConnectionException(ErrorMessage.SSE_CONNECT_FAILED);
        }
        return null;
    }

    public void broadcast(Long userId, Object data, String eventName, List<Relationship> relationships) {
        sseRepository.broadcast(userId, data, eventName, relationships);
    }
}
