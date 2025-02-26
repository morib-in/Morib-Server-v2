package org.morib.server.api.timerView.dto;

import java.util.List;

public record AssignAllowedGroupsRequestDto(
        List<Long> allowedGroupIdList
) {
}
