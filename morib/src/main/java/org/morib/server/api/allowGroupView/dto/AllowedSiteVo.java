package org.morib.server.api.allowGroupView.dto;

import org.morib.server.domain.allowedSite.infra.AllowedSite;

public record AllowedSiteVo(
        String favicon,
        String siteName,
        String pageName,
        String siteUrl
) {
    public static AllowedSiteVo of(AllowedSite allowedSite) {
        return new AllowedSiteVo(allowedSite.getFavicon(), allowedSite.getSiteName(), allowedSite.getPageName(), allowedSite.getSiteUrl());
    }

    public static AllowedSiteVo of(String favicon, String siteName, String pageName, String siteUrl) {
        return new AllowedSiteVo(favicon, siteName, pageName, siteUrl);
    }
}
