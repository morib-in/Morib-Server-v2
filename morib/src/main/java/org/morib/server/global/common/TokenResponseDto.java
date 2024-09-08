package org.morib.server.global.common;

public record TokenResponseDto(
        String accessToken,
        String refreshToken,
        Long userId
) {
    public static TokenResponseDto of(String accessToken, String refreshToken, Long userId) {
        return new TokenResponseDto(accessToken, refreshToken, userId);
    }
}
