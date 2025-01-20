package org.morib.server.domain.allowedSite.application;

import lombok.RequiredArgsConstructor;
import org.morib.server.api.allowGroupView.dto.AllowedSiteVo;
import org.morib.server.domain.allowedGroup.infra.AllowedGroup;
import org.morib.server.domain.allowedSite.application.dto.CreateAllowedSiteServiceDto;
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
    public AllowedSite create(CreateAllowedSiteServiceDto dto) {
        return allowedSiteRepository.save(AllowedSite.create(dto.siteName(), dto.siteUrl(), dto.findAllowedGroup()));
    }

    @Override
    @Transactional
    public void createAll(AllowedGroup allowedGroup, List<AllowedSiteVo> onboardRequestDto) {
        allowedSiteRepository.saveAll(onboardRequestDto.stream()
                .map(allowedSites -> AllowedSite.create(
                        allowedSites.siteName(),
                        allowedSites.siteUrl(),
                        allowedGroup
                ))
                .toList());
    }

}
