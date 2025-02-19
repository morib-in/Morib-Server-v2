package org.morib.server.domain.allowedSite.infra;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AllowedSiteRepository extends JpaRepository<AllowedSite, Long> {
    boolean existsBySiteUrl(String siteUrl);
}
