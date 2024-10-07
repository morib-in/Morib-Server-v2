package org.morib.server.global.common;

public record TokenResponseDto(
        String accessToken,
        Long userId
) {
    public static TokenResponseDto of(String accessToken, Long userId) {
        return new TokenResponseDto(accessToken, userId);
    }
}
