package org.morib.server.domain.allowedSite.application;

import org.morib.server.domain.allowedSite.infra.AllowedSite;

import java.util.List;

public interface FetchAllowedSiteService {
    void isExist(String siteUrl, Long allowedGroupId);
    AllowedSite fetchById(Long id);
    List<AllowedSite> fetchByDomainContaining(Long allowedGroupId, String topDomainUrl);
    AllowedSite fetchBySiteUrlAndAllowedGroupId(String siteUrl, Long allowedGroupId);
    AllowedSite fetchBySiteUrlContainingAndAllowedGroupId(String siteUrl, Long allowedGroupId);
}
