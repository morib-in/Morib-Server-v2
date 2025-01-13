package org.morib.server.api.modalView.dto;

import org.morib.server.domain.user.infra.User;

public record FetchRelationshipRequestsResponseDto(
        Long id,
        String name,
        String email,
        String imageUrl
) {
    public static FetchRelationshipRequestsResponseDto of (User user) {
        return new FetchRelationshipRequestsResponseDto(user.getId(), user.getName(), user.getEmail(), user.getImageUrl());
    }
}
