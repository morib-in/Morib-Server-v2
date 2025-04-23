package org.morib.server.domain.allowedSite.application;

import org.morib.server.api.allowGroupView.dto.AllowedSiteVo;

public interface FetchSiteInfoService {
    AllowedSiteVo fetchSiteMetadataFromUrl(String url);
}
