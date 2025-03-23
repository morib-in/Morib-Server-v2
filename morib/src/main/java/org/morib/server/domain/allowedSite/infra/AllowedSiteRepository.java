package org.morib.server.domain.allowedSite.infra;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AllowedSiteRepository extends JpaRepository<AllowedSite, Long> {
    boolean existsBySiteUrlAndAllowedGroupId(String siteUrl, Long allowedGroupId);
    List<AllowedSite> findByAllowedGroupIdAndSiteUrlContaining(Long allowedGroupId, String siteUrl);
    AllowedSite findBySiteUrlAndAllowedGroupId(String siteUrl, Long allowedGroupId);
    AllowedSite findBySiteUrlContainingAndAllowedGroupId(String siteUrl, Long allowedGroupId);
}
