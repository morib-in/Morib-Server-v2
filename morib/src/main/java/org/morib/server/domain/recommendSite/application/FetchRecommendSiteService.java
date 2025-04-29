package org.morib.server.domain.recommendSite.application;

import org.morib.server.domain.recommendSite.infra.RecommendSite;
import org.morib.server.domain.user.infra.type.InterestArea;

import java.util.List;

public interface FetchRecommendSiteService {
    List<RecommendSite> fetchByInterestArea(InterestArea interestArea);
    RecommendSite fetchBySiteUrl(String siteUrl);
    RecommendSite fetchBySiteUrlContaining(String siteUrl);
    List<RecommendSite> fetchAll();
}
