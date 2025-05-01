package org.morib.server.domain.recommendSite.application;

import lombok.RequiredArgsConstructor;
import org.morib.server.domain.recommendSite.infra.RecommendSite;
import org.morib.server.domain.recommendSite.infra.RecommendSiteRepository;
import org.morib.server.domain.user.infra.type.InterestArea;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FetchRecommendSiteServiceImpl implements FetchRecommendSiteService {

    private final RecommendSiteRepository recommendSiteRepository;

    @Override
    public List<RecommendSite> fetchByInterestArea(InterestArea interestArea) {
        return recommendSiteRepository.findByInterestArea(interestArea);
    }

    @Override
    public RecommendSite fetchBySiteUrl(String siteUrl) {
        return recommendSiteRepository.findBySiteUrl(siteUrl).stream().findFirst().orElse(null);
    }

    @Override
    public RecommendSite fetchBySiteUrlContaining(String siteUrl) {
        return recommendSiteRepository.findBySiteUrlContaining(siteUrl).stream().findFirst().orElse(null);
    }
    @Override
    public List<RecommendSite> fetchAll() {
        return recommendSiteRepository.findAll();
    }
}
