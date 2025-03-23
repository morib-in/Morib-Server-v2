package org.morib.server.domain.allowedSite.application;

import lombok.RequiredArgsConstructor;
import org.morib.server.api.allowGroupView.dto.AllowedSiteVo;
import org.morib.server.domain.allowedGroup.infra.AllowedGroup;
import org.morib.server.domain.allowedSite.infra.AllowedSite;
import org.morib.server.domain.allowedSite.infra.AllowedSiteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CreateAllowedSiteServiceImpl implements CreateAllowedSiteService {

    private final AllowedSiteRepository allowedSiteRepository;

    @Override
    public AllowedSite create(AllowedGroup allowedGroup, AllowedSiteVo allowedSiteVo) {
        return allowedSiteRepository.save(
                AllowedSite.create(allowedGroup, allowedSiteVo.favicon(), allowedSiteVo.siteName(), allowedSiteVo.pageName(), allowedSiteVo.siteUrl()));
    }

    @Override
    @Transactional
    public void createAll(AllowedGroup allowedGroup, List<AllowedSiteVo> allowedSiteVos) {
        allowedSiteRepository.saveAll(allowedSiteVos.stream()
                .map(allowedSites -> AllowedSite.create(
                        allowedGroup,
                        allowedSites.favicon(),
                        allowedSites.siteName(),
                        allowedSites.pageName(),
                        allowedSites.siteUrl()
                ))
                .toList());
    }

}
