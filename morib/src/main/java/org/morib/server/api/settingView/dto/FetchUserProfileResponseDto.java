package org.morib.server.api.settingView.dto;

public record FetchUserProfileResponseDto(
        Long id,
        String name,
        String email,
        String imageUrl,
        boolean isPushEnabled
) {
    public static FetchUserProfileResponseDto of (Long id, String name, String email, String imageUrl, boolean isPushEnabled) {
        return new FetchUserProfileResponseDto(id, name, email, imageUrl, isPushEnabled);

    }
}
