package org.morib.server.domain.allowedSite.application.dto;

import org.morib.server.domain.allowedGroup.infra.AllowedGroup;

public record AddAllowSiteInAllowGroupServiceDto(AllowedGroup findAllowedGroup, String site, String name) {

    public static AddAllowSiteInAllowGroupServiceDto of(AllowedGroup findAllowedGroup, String site, String name) {
        return new AddAllowSiteInAllowGroupServiceDto(findAllowedGroup, site, name);
    }
}
