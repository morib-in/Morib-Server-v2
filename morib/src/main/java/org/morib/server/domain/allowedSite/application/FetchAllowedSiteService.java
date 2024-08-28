package org.morib.server.domain.allowedSite.application;

import java.util.List;
import org.morib.server.domain.allowedSite.infra.AllowedSite;

public interface FetchAllowedSiteService {

    List<AllowedSite> fetchByCategoryId(Long categoryId);

    List<AllowedSite> fetchByTaskId(Long taskId);
}
