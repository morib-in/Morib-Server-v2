package org.morib.server.api.allowGroupView.facade;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.morib.server.annotation.Facade;
import org.morib.server.api.allowGroupView.dto.AllowedSiteVo;
import org.morib.server.api.allowGroupView.dto.CreateAllowedSiteInAllowedGroupRequestDto;
import org.morib.server.api.allowGroupView.dto.FetchAllAllowedGroupsResponseDto;
import org.morib.server.api.allowGroupView.dto.FetchAllowedGroupDetailResponseDto;
import org.morib.server.api.allowGroupView.dto.InterestAreaSiteResponseDto;
import org.morib.server.api.allowGroupView.dto.UpdateAllowedGroupColorCodeRequestDto;
import org.morib.server.api.allowGroupView.dto.UpdateAllowedGroupNameRequestDto;
import org.morib.server.domain.allowedGroup.application.AllowedGroupManager;
import org.morib.server.domain.allowedGroup.application.DeleteAllowedGroupService;
import org.morib.server.domain.allowedGroup.application.FetchAllowedGroupService;
import org.morib.server.domain.allowedGroup.infra.AllowedGroup;
import org.morib.server.domain.allowedSite.application.CreateAllowedSiteService;
import org.morib.server.domain.allowedSite.application.DeleteAllowedSiteService;
import org.morib.server.domain.allowedSite.application.FetchTabNameService;
import org.morib.server.domain.allowedSite.application.dto.CreateAllowedSiteInAllowedGroupServiceDto;
import org.morib.server.domain.user.application.service.FetchUserService;
import org.morib.server.domain.user.infra.User;
import org.morib.server.global.common.ConnectType;
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
    private final FetchUserService fetchUserService;

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
    public void updateAllowedGroupColorCode(Long groupId,
        UpdateAllowedGroupColorCodeRequestDto dto) {
        allowedGroupManager.updateColorCode(fetchAllowedGroupService.findById(groupId),
            dto.colorCode());
    }

    @Transactional
    public void deleteAllowedSite(Long allowedSiteId) {
        deleteAllowedSiteService.deleteAllowedSite(allowedSiteId);
    }


    @Transactional(readOnly = true)
    public List<FetchAllAllowedGroupsResponseDto> getAllowedGroups(Long userId,
        ConnectType connectType) {
        List<AllowedGroup> all = fetchAllowedGroupService.findAllByUserId(userId);

        return all.stream().map(this::madefetchAllAllowedGroupSetsResponseDto).toList();
    }

    private FetchAllAllowedGroupsResponseDto madefetchAllAllowedGroupSetsResponseDto(
        AllowedGroup a) {
        final List<String> allowIcons = new ArrayList<>();
        addSiteIcons(a, allowIcons);
        return FetchAllAllowedGroupsResponseDto.of(a.getName(), a.getColorCode(),
            allowIcons);
    }

    private void addSiteIcons(AllowedGroup a, List<String> allowIcons) {
        a.getAllowedSites().forEach(b -> allowIcons.add(b.getSiteIconUrl()));
    }

    @Transactional(readOnly = true)
    public FetchAllowedGroupDetailResponseDto getGroupDetail(Long groupId,
        ConnectType connectType) {
        AllowedGroup findAllowedGroup = fetchAllowedGroupService.findById(groupId);
        List<AllowedSiteVo> allowedGroupDetailAllowedSiteVos = findAllowedGroup.getAllowedSites()
            .stream().map(AllowedSiteVo::of).toList();

        return FetchAllowedGroupDetailResponseDto.of(findAllowedGroup.getId(),
            findAllowedGroup.getName(), allowedGroupDetailAllowedSiteVos);
    }

    @Transactional(readOnly = true)
    public InterestAreaSiteResponseDto getRecommendSite(Long userId) {
        User user = fetchUserService.fetchByUserId(userId);
        return InterestAreaSiteResponseDto.of(user.getInterestArea().getAreaSiteVos());
    }
}
