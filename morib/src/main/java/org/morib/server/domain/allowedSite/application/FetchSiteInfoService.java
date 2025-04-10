package org.morib.server.domain.allowedSite.application;

import org.morib.server.api.allowGroupView.dto.AllowedSiteVo;

public interface FetchSiteInfoService {
    AllowedSiteVo fetch(String url);
    String getTopDomain(String urlString);
    String getTopDomainUrl(String urlString);
    String getTopDomainWhenParsingFailed(String urlString);
    String normalizeUrl(String url);
    String getDomainExceptHost(String urlString);
}
