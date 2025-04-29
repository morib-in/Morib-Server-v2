package org.morib.server.api.allowGroupView.dto;

import java.util.List;

public record RecommendSiteResponseWrapperDto(
        List<RecommendSiteResponseDto> recommendSites
) {
    public static RecommendSiteResponseWrapperDto from(List<RecommendSiteResponseDto> recommendSites) {
        return new RecommendSiteResponseWrapperDto(recommendSites);
    }
}
