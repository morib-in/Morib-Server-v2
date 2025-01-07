package org.morib.server.api.allowGroupView.dto;

import org.morib.server.domain.allowedSite.infra.AllowedSite;

public record AllowedSiteVo(
    String siteIcon,
    String siteUrl
) {

    public static AllowedSiteVo of(AllowedSite allowedSite) {
        return new AllowedSiteVo(allowedSite.getSiteIconUrl(), allowedSite.getSiteUrl());
    }
}
