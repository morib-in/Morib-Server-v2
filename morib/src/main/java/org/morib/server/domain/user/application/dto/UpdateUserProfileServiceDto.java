package org.morib.server.domain.user.application.dto;

import org.morib.server.api.settingView.dto.UpdateUserProfileRequestDto;

public record UpdateUserProfileServiceDto(
        String name,
        String imageUrl,
        boolean isPushEnabled
) {
    public static UpdateUserProfileServiceDto of (UpdateUserProfileRequestDto updateUserProfileRequestDto) {
        return new UpdateUserProfileServiceDto(updateUserProfileRequestDto.name(), updateUserProfileRequestDto.imageUrl(), updateUserProfileRequestDto.isPushEnabled());
    }
}
