package org.morib.server.domain.allowedSite.infra;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AllowedSiteRepository extends JpaRepository<AllowedSite, Long> {
    boolean existsBySiteUrlAndAllowedGroupId(String siteUrl, Long allowedGroupId);
    List<AllowedSite> findByAllowedGroupIdAndSiteUrlContaining(Long allowedGroupId, String siteUrl);
    AllowedSite findBySiteUrlAndAllowedGroupId(String siteUrl, Long allowedGroupId);
    AllowedSite findBySiteUrlContainingAndAllowedGroupId(String siteUrl, Long allowedGroupId);
}
