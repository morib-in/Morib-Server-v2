package org.morib.server.domain.allowedSite.application;

import org.morib.server.domain.allowedSite.infra.AllowedSite;
import org.morib.server.domain.allowedSite.infra.type.OwnerType;

public interface CreateAllowedSiteService {
    AllowedSite create(String siteName, String siteUrl, OwnerType ownerType, Long ownerId);
}
