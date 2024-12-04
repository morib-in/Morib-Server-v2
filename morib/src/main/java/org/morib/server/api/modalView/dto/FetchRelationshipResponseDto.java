package org.morib.server.api.modalView.dto;

import org.morib.server.domain.user.infra.User;

public record FetchRelationshipResponseDto(
        Long id,
        String name,
        String email,
        String imageUrl
) {
    public static FetchRelationshipResponseDto of (User user) {
        return new FetchRelationshipResponseDto(user.getId(), user.getName(), user.getEmail(), user.getImageUrl());
    }
}
