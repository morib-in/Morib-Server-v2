package org.morib.server.api.allowGroupView.dto;

import org.morib.server.domain.allowedSite.infra.AllowedSite;

public record FetchAllowedGroupDetailAllowedSiteVo(
    String siteIcon,
    String siteUrl
) {

    public static FetchAllowedGroupDetailAllowedSiteVo of(AllowedSite allowedSite) {
        return new FetchAllowedGroupDetailAllowedSiteVo(allowedSite.getSiteIconUrl(), allowedSite.getSiteUrl());
    }
}
