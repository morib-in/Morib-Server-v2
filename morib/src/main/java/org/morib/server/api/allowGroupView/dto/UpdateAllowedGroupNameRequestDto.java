package org.morib.server.api.allowGroupView.dto;

public record UpdateAllowedGroupNameRequestDto(String name) {
    public static UpdateAllowedGroupNameRequestDto of(String name) {
        return new UpdateAllowedGroupNameRequestDto(name);
    }
}
