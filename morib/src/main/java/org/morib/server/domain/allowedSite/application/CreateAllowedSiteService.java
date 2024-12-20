package org.morib.server.domain.allowedSite.application;

import org.morib.server.domain.allowedSite.application.dto.CreateAllowedSiteInAllowedGroupServiceDto;
import org.morib.server.domain.allowedSite.infra.AllowedSite;

public interface CreateAllowedSiteService {
    AllowedSite create(String siteName, String siteUrl);

    AllowedSite create(
        CreateAllowedSiteInAllowedGroupServiceDto createAllowedSiteInAllowedGroupServiceDto);

}
