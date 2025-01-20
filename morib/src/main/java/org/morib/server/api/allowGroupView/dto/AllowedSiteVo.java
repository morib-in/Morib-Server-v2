package org.morib.server.api.allowGroupView.dto;

import org.morib.server.domain.allowedSite.infra.AllowedSite;

public record AllowedSiteVo(
    String siteName,
    String siteUrl
) {

    public static AllowedSiteVo of(AllowedSite allowedSite) {
        return new AllowedSiteVo(allowedSite.getSiteName(), allowedSite.getSiteUrl());
    }
}
