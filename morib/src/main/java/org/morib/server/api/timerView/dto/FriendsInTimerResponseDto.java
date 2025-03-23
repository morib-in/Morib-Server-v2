package org.morib.server.api.timerView.dto;

import org.morib.server.api.modalView.dto.FetchRelationshipResponseDto;
import org.morib.server.domain.timer.infra.TimerStatus;

public record FriendsInTimerResponseDto(
        Long id,
        String name,
        String imageUrl,
        int elapsedTime,
        String categoryName,
        boolean isOnline,
        TimerStatus timerStatus
) {
    public static FriendsInTimerResponseDto of (FetchRelationshipResponseDto fetchRelationshipResponseDto, int elapsedTime, String categoryName, TimerStatus timerStatus) {
        return new FriendsInTimerResponseDto(fetchRelationshipResponseDto.id(), fetchRelationshipResponseDto.name(), fetchRelationshipResponseDto.imageUrl(), elapsedTime, categoryName, fetchRelationshipResponseDto.isOnline(), timerStatus);
    }

    public static FriendsInTimerResponseDto of (Long id, String name, String imageUrl, int elapsedTime, String categoryName, boolean isOnline, TimerStatus timerStatus) {
        return new FriendsInTimerResponseDto(id, name, imageUrl, elapsedTime, categoryName, isOnline, timerStatus);
    }


}
