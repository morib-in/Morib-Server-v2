package org.morib.server.domain.recommendSite.infra;

import org.morib.server.domain.user.infra.type.InterestArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecommendSiteRepository extends JpaRepository<RecommendSite, Long> {
    List<RecommendSite> findByInterestArea(InterestArea interestArea);
    List<RecommendSite> findBySiteUrl(String siteUrl);
    List<RecommendSite> findBySiteUrlContaining(String siteUrl);
}
