package org.morib.server.api.allowGroupView.facade;

import lombok.RequiredArgsConstructor;
import org.morib.server.annotation.Facade;
import org.morib.server.api.allowGroupView.dto.UpdateAllowedGroupColorCodeRequestDto;
import org.morib.server.api.allowGroupView.dto.UpdateAllowedGroupNameRequestDto;
import org.morib.server.domain.allowedGroup.application.DeleteAllowedGroupService;
import org.morib.server.api.allowGroupView.dto.CreateAllowedSiteInAllowedGroupRequestDto;
import org.morib.server.domain.allowedGroup.application.FetchAllowedGroupService;
import org.morib.server.domain.allowedGroup.application.AllowedGroupManager;
import org.morib.server.domain.allowedGroup.infra.AllowedGroup;
import org.morib.server.domain.allowedSite.application.CreateAllowedSiteService;
import org.morib.server.domain.allowedSite.application.DeleteAllowedSiteService;
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
    private final AllowedGroupManager allowedGroupManager;
    private final DeleteAllowedSiteService deleteAllowedSiteService;

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

    @Transactional
    public void updateAllowedGroupName(Long groupId, UpdateAllowedGroupNameRequestDto dto) {
        allowedGroupManager.updateName(fetchAllowedGroupService.findById(groupId), dto.name());
    }

    @Transactional
    public void updateAllowedGroupColorCode(Long groupId, UpdateAllowedGroupColorCodeRequestDto dto) {
        allowedGroupManager.updateColorCode(fetchAllowedGroupService.findById(groupId), dto.colorCode());
    }

    @Transactional
    public void deleteAllowedSite(Long allowedSiteId) {
        deleteAllowedSiteService.deleteAllowedSite(allowedSiteId);
    }
}
