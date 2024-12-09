package org.morib.server.domain.allowedSite.application;

import lombok.RequiredArgsConstructor;
import org.morib.server.domain.allowedSite.application.dto.CreateAllowedSiteInAllowedGroupServiceDto;
import org.morib.server.domain.allowedSite.infra.AllowedSite;
import org.morib.server.domain.allowedSite.infra.AllowedSiteRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateAllowedSiteServiceImpl implements CreateAllowedSiteService {
    private final AllowedSiteRepository allowedSiteRepository;

    @Override
    public AllowedSite create(String siteName, String siteUrl) {
        return allowedSiteRepository.save(AllowedSite.create(siteName, siteUrl));
    }

    @Override
    public AllowedSite create(CreateAllowedSiteInAllowedGroupServiceDto dto) {
        return allowedSiteRepository.save(AllowedSite.create(dto.site(), dto.name(), dto.findAllowedGroup()));
    }
}
