package org.morib.server.api.allowGroupView.dto;

import org.morib.server.domain.allowedGroup.infra.AllowedGroup;

public record CreateAllowedGroupResponse(
        Long id,
        String name,
        String colorCode
) {
    public static CreateAllowedGroupResponse of(AllowedGroup allowedGroup) {
        return new CreateAllowedGroupResponse(allowedGroup.getId(), allowedGroup.getName(), allowedGroup.getColorCode());
    }
}
