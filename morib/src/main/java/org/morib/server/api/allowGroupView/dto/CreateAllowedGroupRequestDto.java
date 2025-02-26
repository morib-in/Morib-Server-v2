package org.morib.server.api.allowGroupView.dto;

public record CreateAllowedGroupRequestDto(
        String name,
        String colorCode
) {
    public static CreateAllowedGroupRequestDto of (String name, String colorCode) {
        return new CreateAllowedGroupRequestDto(name, colorCode);
    }
}
