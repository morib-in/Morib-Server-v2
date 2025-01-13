package org.morib.server.domain.user.application.dto;

public record ReissueTokenServiceDto(
        String accessToken,
        String refreshToken
) {
    public static ReissueTokenServiceDto of(String accessToken, String refreshToken) {
        return new ReissueTokenServiceDto(accessToken, refreshToken);
    }
}
