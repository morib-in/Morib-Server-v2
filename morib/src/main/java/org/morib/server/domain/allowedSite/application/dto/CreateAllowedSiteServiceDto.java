package org.morib.server.domain.allowedSite.application.dto;

import org.morib.server.domain.allowedGroup.infra.AllowedGroup;

public record CreateAllowedSiteServiceDto(
        AllowedGroup findAllowedGroup,
        String siteUrl,
        String siteName) {

    public static CreateAllowedSiteServiceDto of(AllowedGroup findAllowedGroup, String siteUrl, String siteName) {
        return new CreateAllowedSiteServiceDto(findAllowedGroup, siteUrl, siteName);
    }
}
