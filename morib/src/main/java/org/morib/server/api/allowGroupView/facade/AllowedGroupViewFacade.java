package org.morib.server.api.allowGroupView.facade;

import lombok.RequiredArgsConstructor;
import org.morib.server.annotation.Facade;
import org.morib.server.api.allowGroupView.dto.AddAllowSiteInAllowGroupRequestDto;
import org.morib.server.domain.allowedGroup.application.AllowedGroupService;
import org.morib.server.domain.allowedGroup.infra.AllowedGroup;
import org.morib.server.domain.allowedSite.application.CreateAllowedSiteService;
import org.morib.server.domain.allowedSite.application.FetchTabNameService;
import org.morib.server.domain.allowedSite.application.dto.AddAllowSiteInAllowGroupServiceDto;
import org.springframework.transaction.annotation.Transactional;

@Facade
@RequiredArgsConstructor
public class AllowedGroupViewFacade {

    private final AllowedGroupService allowedGroupService;
    private final FetchTabNameService fetchTabNameService;
    private final CreateAllowedSiteService createAllowedSiteService;

    @Transactional // 이후 분리 진행했을때는 해당 point에서 분산 트랜잭션 관련해서 고려해볼 수 있는 부분
    public void deleteAllowServiceSet(Long groupId) {
        allowedGroupService.deleteAllowedGroupById(groupId);
    }

    @Transactional
    public void addAllowedSite(
        AddAllowSiteInAllowGroupRequestDto addAllowSiteInAllowGroupRequestDto) {

        AllowedGroup findAllowedGroup = allowedGroupService.findById(
            addAllowSiteInAllowGroupRequestDto.groupId());
        String site = addAllowSiteInAllowGroupRequestDto.allowedSiteUrl();
        String name = fetchTabNameService.fetch(site);

        createAllowedSiteService.create(
            AddAllowSiteInAllowGroupServiceDto.of(findAllowedGroup, site, name));
    }
}
