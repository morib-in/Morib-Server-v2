package org.morib.server.domain.allowedSite.application.dto;

import org.morib.server.api.allowGroupView.dto.AllowedSiteVo;
import org.morib.server.domain.allowedGroup.infra.AllowedGroup;

public record CreateAllowedSiteServiceDto(
        AllowedGroup findAllowedGroup,
        String favicon,
        String siteName,
        String pageName,
        String siteUrl
) {
    public static CreateAllowedSiteServiceDto of(AllowedGroup findAllowedGroup, String favicon, String siteName, String pageName, String siteUrl) {
        return new CreateAllowedSiteServiceDto(findAllowedGroup, favicon, siteName, pageName, siteUrl);
    }
    public static CreateAllowedSiteServiceDto of(AllowedGroup findAllowedGroup, AllowedSiteVo allowedSiteVo) {
        return new CreateAllowedSiteServiceDto(findAllowedGroup, allowedSiteVo.favicon(), allowedSiteVo.siteName(), allowedSiteVo.pageName(), allowedSiteVo.siteUrl());
    }
}
