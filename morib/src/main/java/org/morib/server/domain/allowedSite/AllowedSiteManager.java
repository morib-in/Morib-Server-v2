package org.morib.server.domain.allowedSite;

import org.morib.server.annotation.Manager;
import org.morib.server.api.allowGroupView.dto.AllowedSiteVo;
import org.morib.server.domain.allowedSite.infra.AllowedSite;
import org.springframework.transaction.annotation.Transactional;

@Manager
public class AllowedSiteManager {

    @Transactional
    public void updateAllowedSiteUrl(AllowedSite allowedSite, String siteUrl) {
        allowedSite.updateSiteUrl(siteUrl);
    }

    @Transactional
    public void updateAllowedSiteInfo(AllowedSite allowedSite, AllowedSiteVo allowedSiteVo) {
        allowedSite.updateAllowedSiteInfo(allowedSiteVo.favicon(), allowedSiteVo.siteName(), allowedSiteVo.pageName(), allowedSiteVo.siteUrl());
    }
}
