package org.morib.server.api.modalView.dto;

import org.morib.server.domain.user.infra.User;

public record FetchFriendsResponseDto(
        Long id,
        String name,
        String email,
        String imageUrl
) {
    public static FetchFriendsResponseDto of (User user) {
        return new FetchFriendsResponseDto(user.getId(), user.getName(), user.getEmail(), user.getImageUrl());
    }
}
