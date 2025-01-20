package org.morib.server.api.timerView.dto;

import org.morib.server.api.modalView.dto.FetchRelationshipResponseDto;

public record FriendsInTimerResponseDto(
        Long id,
        String name,
        String imageUrl,
        int elapsedTime,
        String categoryName,
        boolean isOnline
) {
    public static FriendsInTimerResponseDto of (FetchRelationshipResponseDto fetchRelationshipResponseDto, String categoryName) {
        return new FriendsInTimerResponseDto(fetchRelationshipResponseDto.id(), fetchRelationshipResponseDto.name(), fetchRelationshipResponseDto.imageUrl(), fetchRelationshipResponseDto.elapsedTime(), categoryName, fetchRelationshipResponseDto.isOnline());
    }
}
