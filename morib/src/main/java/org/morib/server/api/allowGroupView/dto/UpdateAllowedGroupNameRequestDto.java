package org.morib.server.api.allowGroupView.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateAllowedGroupNameRequestDto(
        @NotBlank
        String name
)
{
    public static UpdateAllowedGroupNameRequestDto of(String name) {
        return new UpdateAllowedGroupNameRequestDto(name);
    }
}
