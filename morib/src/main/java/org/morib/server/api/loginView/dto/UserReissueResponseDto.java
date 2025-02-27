package org.morib.server.api.loginView.dto;

public record UserReissueResponseDto(
        String accessToken
) {
    public static UserReissueResponseDto of(String accessToken) {
        return new UserReissueResponseDto(accessToken);
    }
}
