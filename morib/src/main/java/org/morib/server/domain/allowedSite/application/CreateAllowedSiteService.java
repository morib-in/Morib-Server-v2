package org.morib.server.domain.allowedSite.application;

import org.morib.server.api.allowGroupView.dto.AllowedSiteVo;
import org.morib.server.domain.allowedGroup.infra.AllowedGroup;
import org.morib.server.domain.allowedSite.application.dto.CreateAllowedSiteServiceDto;
import org.morib.server.domain.allowedSite.infra.AllowedSite;

import java.util.List;

public interface CreateAllowedSiteService {
    AllowedSite create(CreateAllowedSiteServiceDto createAllowedSiteServiceDto);
    void createAll(AllowedGroup allowedGroup, List<AllowedSiteVo> onboardRequestDto);
}
