package org.morib.server.api.allowGroupView.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateAllowedGroupColorCodeRequestDto(
        @NotBlank
        String colorCode
) {
    public static UpdateAllowedGroupColorCodeRequestDto of(String colorCode) {
        return new UpdateAllowedGroupColorCodeRequestDto(colorCode);
    }
}
