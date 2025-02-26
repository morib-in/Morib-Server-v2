package org.morib.server.api.allowGroupView.dto;

import org.morib.server.domain.allowedSite.infra.AllowedSite;

public record AllowedSiteWithIdVo(
        Long id,
        String favicon,
        String siteName,
        String pageName,
        String siteUrl
) {
    public static AllowedSiteWithIdVo of(AllowedSite allowedSite) {
        return new AllowedSiteWithIdVo(allowedSite.getId(), allowedSite.getFavicon(), allowedSite.getSiteName(), allowedSite.getPageName(), allowedSite.getSiteUrl());
    }
}
