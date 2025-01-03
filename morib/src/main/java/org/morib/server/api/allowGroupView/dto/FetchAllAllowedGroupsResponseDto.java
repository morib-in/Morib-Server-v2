package org.morib.server.api.allowGroupView.dto;

import java.util.List;

public record FetchAllAllowedGroupsResponseDto(String name, String colorCode, List<String> allowSitesIcon) {
    public static FetchAllAllowedGroupsResponseDto of(String name, String colorCode, List<String> allowSitesIcon) {
        return new FetchAllAllowedGroupsResponseDto(name, colorCode, allowSitesIcon);
    }
}
