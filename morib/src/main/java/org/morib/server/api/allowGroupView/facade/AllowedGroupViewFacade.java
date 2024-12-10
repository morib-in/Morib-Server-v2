package org.morib.server.api.allowGroupView.facade;

import lombok.RequiredArgsConstructor;
import org.morib.server.annotation.Facade;
import org.morib.server.domain.allowedGroup.application.DeleteAllowedGroupService;
import org.morib.server.api.allowGroupView.dto.CreateAllowedSiteInAllowedGroupRequestDto;
import org.morib.server.domain.allowedGroup.application.FetchAllowedGroupService;
import org.morib.server.domain.allowedGroup.infra.AllowedGroup;
import org.morib.server.domain.allowedSite.application.CreateAllowedSiteService;
import org.morib.server.domain.allowedSite.application.FetchTabNameService;
import org.morib.server.domain.allowedSite.application.dto.CreateAllowedSiteInAllowedGroupServiceDto;
import org.springframework.transaction.annotation.Transactional;

@Facade
@RequiredArgsConstructor
public class AllowedGroupViewFacade {

    private final DeleteAllowedGroupService deleteAllowedGroupService;
    private final FetchTabNameService fetchTabNameService;
    private final FetchAllowedGroupService fetchAllowedGroupService;
    private final CreateAllowedSiteService createAllowedSiteService;

    @Transactional // 이후 분리 진행했을때는 해당 point에서 분산 트랜잭션 관련해서 고려해볼 수 있는 부분
    public void deleteAllowedServiceSet(Long groupId) {
        deleteAllowedGroupService.deleteAllowedGroupById(groupId);
    }

    @Transactional
    public void addAllowedSite(
        CreateAllowedSiteInAllowedGroupRequestDto createAllowedSiteInAllowedGroupRequestDto) {

        AllowedGroup findAllowedGroup = fetchAllowedGroupService.findById(
            createAllowedSiteInAllowedGroupRequestDto.groupId());
        String site = createAllowedSiteInAllowedGroupRequestDto.allowedSiteUrl();
        String name = fetchTabNameService.fetch(site);

        createAllowedSiteService.create(
            CreateAllowedSiteInAllowedGroupServiceDto.of(findAllowedGroup, site, name));
    }
}
