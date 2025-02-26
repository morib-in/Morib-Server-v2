package org.morib.server.api.allowGroupView.dto;

import java.util.List;

public record FetchAllowedGroupListResponseDto(
        Long id,
        String name,
        String colorCode,
        List<String> allowedSites,
        int extraCnt) {
    public static FetchAllowedGroupListResponseDto of(Long id, String name, String colorCode, List<String> allowedSites, int extraCnt) {
        return new FetchAllowedGroupListResponseDto(id, name, colorCode, allowedSites, extraCnt);
    }
}
