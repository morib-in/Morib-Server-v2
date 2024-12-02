package org.morib.server.api.settingView.dto;

public record UpdateUserProfileRequestDto(
        String name,
        String imageUrl,
        boolean isPushEnabled
) {
}
