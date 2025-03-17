package org.morib.server.global.sse.application.service;

import lombok.RequiredArgsConstructor;
import org.morib.server.domain.relationship.application.FetchRelationshipService;
import org.morib.server.domain.relationship.infra.Relationship;
import org.morib.server.global.exception.SSEConnectionException;
import org.morib.server.global.message.ErrorMessage;
import org.morib.server.global.sse.api.UserInfoDtoForSseUserInfoWrapper;
import org.morib.server.global.sse.application.event.SseDisconnectEvent;
import org.morib.server.global.sse.application.repository.SseRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.morib.server.global.common.Constants.SSE_EVENT_CONNECT;

@Service
@RequiredArgsConstructor
public class SseService {

    private final SseRepository sseRepository;

    public SseEmitter create() {
        return sseRepository.create();
    }

    public SseEmitter add(Long userId, SseEmitter emitter) {
        return sseRepository.add(userId, emitter, 0, "", null);
    }

    public void saveSseUserInfo(Long userId, SseEmitter emitter, UserInfoDtoForSseUserInfoWrapper calculatedSseUserInfoWrapper) {
        sseRepository.add(
                userId,
                emitter,
                calculatedSseUserInfoWrapper.elapsedTime(),
                calculatedSseUserInfoWrapper.runningCategoryName(),
                calculatedSseUserInfoWrapper.taskId());
    }

    public List<SseEmitter> fetchConnectedSseEmittersById(List<Long> ids) {
        return ids.stream()
                .filter(sseRepository::isConnected)
                .map(sseRepository::getSseEmitterById)
                .toList();
    }

    public SseEmitter fetchSseEmitterByUserId(Long userId) {
        return sseRepository.getSseEmitterById(userId);
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

    public boolean validateConnection(Long userId) {
        return sseRepository.isConnected(userId);
    }

    public List<SseEmitter> fetchAllConnectedSseEmitters() {
        return sseRepository.getAllSseEmitters();
    }

    public int getConnectionCount() {
        return sseRepository.getConnectionCount();
    }

    public Set<Long> getConnectedUserIds() {
        return sseRepository.getConnectedUserIds();
    }
}
