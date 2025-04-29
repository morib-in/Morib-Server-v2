package org.morib.server.api.allowGroupView.dto;

import org.morib.server.domain.recommendSite.infra.RecommendSite;

public record RecommendSiteResponseDto(
        String favicon,
        String siteName,
        String pageName,
        String siteUrl
) {
    public static RecommendSiteResponseDto from(RecommendSite recommendSite) {
        return new RecommendSiteResponseDto(
                recommendSite.getFavicon(),
                recommendSite.getSiteName(),
                recommendSite.getPageName(),
                recommendSite.getSiteUrl()
        );
    }
}
