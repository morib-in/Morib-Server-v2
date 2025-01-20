package org.morib.server.api.allowGroupView.dto;

import java.util.List;

public record FetchAllowedGroupListResponseDto(String name, String colorCode, List<String> allowedSites) {
    public static FetchAllowedGroupListResponseDto of(String name, String colorCode, List<String> allowedSites) {
        return new FetchAllowedGroupListResponseDto(name, colorCode, allowedSites);
    }
}
