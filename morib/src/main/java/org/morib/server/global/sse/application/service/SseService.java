package org.morib.server.global.sse.application.service;

import lombok.RequiredArgsConstructor;
import org.morib.server.global.exception.SSEConnectionException;
import org.morib.server.global.message.ErrorMessage;
import org.morib.server.global.sse.application.repository.SseRepository;
import org.morib.server.global.sse.application.repository.SseUserInfoWrapper;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class SseService {

    private final SseRepository sseRepository;

    public SseEmitter create() {
        return sseRepository.create();
    }

    public SseEmitter add(Long userId, SseEmitter emitter) {
        return sseRepository.addSseEmitter(userId, emitter);
    }

    public void removeExistingEmitter(Long userId) {
        sseRepository.removeExistingEmitter(userId);
    }
    public void saveSseUserInfoWrapper(Long userId, SseUserInfoWrapper calculatedSseUserInfoWrapper) {
        sseRepository.mergeUserInfoWrapper(userId, calculatedSseUserInfoWrapper);
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
            if (sseRepository.getConnectedUserIds().contains(userId)) {
                return sseRepository.getSseUserInfoWrapperById(userId).getRunningCategoryName();
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
