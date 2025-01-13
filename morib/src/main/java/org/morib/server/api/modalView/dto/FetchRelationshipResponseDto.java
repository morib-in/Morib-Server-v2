package org.morib.server.api.modalView.dto;

import org.morib.server.domain.user.infra.User;

public record FetchRelationshipResponseDto(
        Long id,
        String name,
        String email,
        String imageUrl,
        boolean isOnline,
        int elapsedTime
) {
    public static FetchRelationshipResponseDto of (User user, boolean isOnline, int elapsedTime) {
        return new FetchRelationshipResponseDto(user.getId(), user.getName(), user.getEmail(), user.getImageUrl(), isOnline, elapsedTime);
    }
}
