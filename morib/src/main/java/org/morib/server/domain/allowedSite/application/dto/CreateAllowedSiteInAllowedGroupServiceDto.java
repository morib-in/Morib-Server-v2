package org.morib.server.domain.allowedSite.application.dto;

import org.morib.server.domain.allowedGroup.infra.AllowedGroup;

public record CreateAllowedSiteInAllowedGroupServiceDto(AllowedGroup findAllowedGroup, String site, String name) {

    public static CreateAllowedSiteInAllowedGroupServiceDto of(AllowedGroup findAllowedGroup, String site, String name) {
        return new CreateAllowedSiteInAllowedGroupServiceDto(findAllowedGroup, site, name);
    }
}
