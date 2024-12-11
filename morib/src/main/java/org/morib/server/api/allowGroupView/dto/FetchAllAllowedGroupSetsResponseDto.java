package org.morib.server.api.allowGroupView.dto;

import java.util.List;

public record FetchAllAllowedGroupSetsResponseDto(String name, String colorCode, List<String> allowSitesIcon) {
    public static FetchAllAllowedGroupSetsResponseDto of(String name, String colorCode, List<String> allowSitesIcon) {
        return new FetchAllAllowedGroupSetsResponseDto(name, colorCode, allowSitesIcon);
    }
}
