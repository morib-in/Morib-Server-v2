package org.morib.server.domain.allowedSite.application;

import org.morib.server.domain.allowedGroup.infra.AllowedGroup;
import org.morib.server.domain.allowedSite.application.dto.AddAllowSiteInAllowGroupServiceDto;
import org.morib.server.domain.allowedSite.infra.AllowedSite;

public interface CreateAllowedSiteService {
    AllowedSite create(String siteName, String siteUrl);

    AllowedSite create(AddAllowSiteInAllowGroupServiceDto addAllowSiteInAllowGroupServiceDto);

}
