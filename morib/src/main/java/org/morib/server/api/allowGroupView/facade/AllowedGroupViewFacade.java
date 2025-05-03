package org.morib.server.api.allowGroupView.facade;

import lombok.RequiredArgsConstructor;
import org.morib.server.annotation.Facade;
import org.morib.server.api.allowGroupView.dto.*;
import org.morib.server.domain.allowedGroup.application.CreateAllowedGroupService;
import org.morib.server.domain.allowedGroup.application.DeleteAllowedGroupService;
import org.morib.server.domain.allowedGroup.application.FetchAllowedGroupService;
import org.morib.server.domain.allowedGroup.infra.AllowedGroup;
import org.morib.server.domain.allowedGroup.infra.AllowedGroupManager;
import org.morib.server.domain.allowedSite.AllowedSiteManager;
import org.morib.server.domain.allowedSite.application.CreateAllowedSiteService;
import org.morib.server.domain.allowedSite.application.DeleteAllowedSiteService;
import org.morib.server.domain.allowedSite.application.FetchAllowedSiteService;
import org.morib.server.domain.allowedSite.application.FetchSiteInfoService;
import org.morib.server.domain.allowedSite.infra.AllowedSite;
import org.morib.server.domain.recentAllowedGroup.application.DeleteRecentAllowedGroupService;
import org.morib.server.domain.recommendSite.application.FetchRecommendSiteService;
import org.morib.server.domain.recommendSite.infra.RecommendSite;
import org.morib.server.domain.user.UserManager;
import org.morib.server.domain.user.application.service.FetchUserService;
import org.morib.server.domain.user.infra.User;
import org.morib.server.domain.user.infra.type.InterestArea;
import org.morib.server.global.common.ConnectType;
import org.morib.server.global.common.util.UrlUtils;
import org.morib.server.global.exception.DuplicateResourceException;
import org.morib.server.global.message.ErrorMessage;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static org.morib.server.global.common.Constants.MAX_VISIBLE_ALLOWED_SERVICES;

@Facade
@RequiredArgsConstructor
public class AllowedGroupViewFacade {

    private final DeleteAllowedGroupService deleteAllowedGroupService;
    private final FetchSiteInfoService fetchSiteInfoService;
    private final FetchAllowedGroupService fetchAllowedGroupService;
    private final CreateAllowedSiteService createAllowedSiteService;
    private final AllowedGroupManager allowedGroupManager;
    private final DeleteAllowedSiteService deleteAllowedSiteService;
    private final FetchUserService fetchUserService;
    private final UserManager userManager;
    private final CreateAllowedGroupService createAllowedGroupService;
    private final FetchAllowedSiteService fetchAllowedSiteService;
    private final AllowedSiteManager allowedSiteManager;
    private final FetchRecommendSiteService fetchRecommendSiteService;

    public CreateAllowedGroupResponse createAllowedGroup(Long userId) {
        User findUser = fetchUserService.fetchByUserId(userId);
        return CreateAllowedGroupResponse.of(createAllowedGroupService.create(findUser, fetchAllowedGroupService.getCounts(userId) + 1));
    }

    public CreateAllowedGroupResponse createAllowedGroupWithBody(Long userId, CreateAllowedGroupRequestDto createAllowedGroupRequestDto) {
        User findUser = fetchUserService.fetchByUserId(userId);
        return CreateAllowedGroupResponse.of(createAllowedGroupService.createWithBody(findUser, createAllowedGroupRequestDto.name(), createAllowedGroupRequestDto.colorCode()));
    }

    @Transactional(readOnly = true)
    public List<FetchAllowedGroupListResponseDto> fetchAllowedGroupList(Long userId, ConnectType connectType) {
        List<AllowedGroup> allowedGroups = fetchAllowedGroupService.findAllByUserId(userId);
        return allowedGroups.stream().map(this::buildFetchAllowedGroupListResponseDto).toList();
    }

    private FetchAllowedGroupListResponseDto buildFetchAllowedGroupListResponseDto(AllowedGroup allowedGroup) {
        return FetchAllowedGroupListResponseDto.of(
                allowedGroup.getId(),
                allowedGroup.getName(),
                allowedGroup.getColorCode(),
                getTop5FaviconInAllowedGroup(allowedGroup),
                getExtraCountByAllowedService(allowedGroup.getAllowedSites().size())
                );
    }

    private List<String> getTop5FaviconInAllowedGroup(AllowedGroup allowedGroup) {
        return filteredAllowedSitesByDomain(allowedGroup).values().stream()
                .flatMap(List::stream)
                .map(AllowedSite::getFavicon)
                .limit(MAX_VISIBLE_ALLOWED_SERVICES)
                .toList();
    }

    private int getExtraCountByAllowedService(int size) {
        return Math.max(size - MAX_VISIBLE_ALLOWED_SERVICES, 0);
    }

    @Transactional(readOnly = true)
    public FetchAllowedGroupResponseDto fetchAllowedGroup(Long groupId, ConnectType connectType) {
        AllowedGroup findAllowedGroup = fetchAllowedGroupService.findById(groupId);

        List<AllowedSiteWithIdVo> allowedSiteVoList = filterByTopDomainAndGetAllowedSiteWithIdVo(findAllowedGroup);

        return FetchAllowedGroupResponseDto.of(
                findAllowedGroup.getId(),
                findAllowedGroup.getName(),
                findAllowedGroup.getColorCode(),
                allowedSiteVoList);
    }

    public List<AllowedSiteWithIdVo> filterByTopDomainAndGetAllowedSiteWithIdVo(AllowedGroup findAllowedGroup) {
        TreeMap<String, List<AllowedSite>> filteredAllowedSites = filteredAllowedSitesByDomain(findAllowedGroup);

        return filteredAllowedSites.values().stream()
                .flatMap(List::stream)
                .map(AllowedSiteWithIdVo::of)
                .toList();
    }

    private TreeMap<String, List<AllowedSite>> filteredAllowedSitesByDomain(AllowedGroup findAllowedGroup) {
        TreeMap<String, List<AllowedSite>> filteredAllowedSites = new TreeMap<>();
        for (AllowedSite allowedSite : findAllowedGroup.getAllowedSites()) {
            String domainForKey = UrlUtils.getTopDomain(allowedSite.getSiteUrl());
            filteredAllowedSites
                    .computeIfAbsent(domainForKey, k -> new ArrayList<>())
                    .add(allowedSite);
        }

        filteredAllowedSites.values().forEach(
                list -> list.sort(Comparator.comparing(AllowedSite::getSiteUrl)));
        return filteredAllowedSites;
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

    @Transactional // 이후 분리 진행했을때는 해당 point에서 분산 트랜잭션 관련해서 고려해볼 수 있는 부분
    public void deleteAllowedGroup(Long groupId) {
        deleteAllowedGroupService.deleteAllowedGroupById(groupId);
    }

    @Transactional(readOnly = true)
    public RecommendSiteResponseWrapperDto fetchRecommendSites(Long userId, Optional<Long> allowedGroupId) {
        InterestArea interestAreaOfUser = fetchUserService.fetchByUserId(userId).getInterestArea();

        if (allowedGroupId.isPresent()) {
            Set<String> findAllowedSiteUrl = fetchAllowedSiteService.fetchByAllowedGroupId(allowedGroupId.get())
                    .stream()
                    .map(AllowedSite::getSiteUrl)
                    .collect(Collectors.toUnmodifiableSet());
            return RecommendSiteResponseWrapperDto.from(fetchRecommendSiteService.fetchByInterestArea(Objects.isNull(interestAreaOfUser) ? InterestArea.OTHERS : interestAreaOfUser)
                    .stream()
                    .filter(recommendSite -> !findAllowedSiteUrl.contains(recommendSite.getSiteUrl()))
                    .map(RecommendSiteResponseDto::from)
                    .toList());
        }

        return RecommendSiteResponseWrapperDto.from(fetchRecommendSiteService.fetchByInterestArea(Objects.isNull(interestAreaOfUser) ? InterestArea.OTHERS : interestAreaOfUser)
                .stream()
                .map(RecommendSiteResponseDto::from)
                .toList());
    }

    @Transactional
    public void createAllowedSite(Long allowedGroupId, AllowedSiteRequestDto allowedSiteRequestDto) {
        AllowedGroup findAllowedGroup = fetchAllowedGroupService.findById(allowedGroupId);
        String originalUrl = allowedSiteRequestDto.siteUrl();
        String normalizedUrl = UrlUtils.normalizeUrl(originalUrl);
        checkDuplicateAllowedSite(normalizedUrl, allowedGroupId);
        try {
            AllowedSiteVo voToSave = determineAllowedSiteVoToSave(originalUrl, normalizedUrl);
            if (voToSave.siteName().isEmpty() || voToSave.pageName().isEmpty()) {
                AllowedSiteVo voToSaveIfNameIsEmpty = AllowedSiteVo.of(
                        voToSave.favicon().isEmpty() ? "" : voToSave.favicon(),
                        UrlUtils.getTopDomain(originalUrl),
                        UrlUtils.getTopDomain(originalUrl),
                        originalUrl
                );
                createAllowedSiteService.create(findAllowedGroup, voToSaveIfNameIsEmpty);
            }
            else createAllowedSiteService.create(findAllowedGroup, voToSave);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateResourceException(ErrorMessage.DUPLICATE_RESOURCE);
        } catch (Exception e) {
            AllowedSiteVo voToSave = AllowedSiteVo.of(
                    "",
                    UrlUtils.getTopDomain(originalUrl),
                    UrlUtils.getTopDomain(originalUrl),
                    originalUrl
            );
            createAllowedSiteService.create(findAllowedGroup, voToSave);
        }
    }

    private AllowedSiteVo determineAllowedSiteVoToSave(String originalUrl, String normalizedUrl) {
        RecommendSite findRecommendSite = fetchRecommendSiteService.fetchBySiteUrl(normalizedUrl);
        if (!Objects.isNull(findRecommendSite)) {
            return AllowedSiteVo.of(
                    findRecommendSite.getFavicon(),
                    findRecommendSite.getSiteName(),
                    findRecommendSite.getPageName(),
                    normalizedUrl
            );
        }
        AllowedSiteVo voToSave = fetchSiteInfoService.fetchSiteMetadataFromUrl(UrlUtils.normalizeUrlForFavicon(originalUrl));
        String faviconToUse;
        if (isFaviconValid(voToSave.favicon())) faviconToUse = voToSave.favicon();
        else {
            RecommendSite similarSiteCandidate = fetchRecommendSiteService.fetchBySiteUrlContaining(normalizedUrl);
            faviconToUse = similarSiteCandidate.getFavicon();
        }
        return AllowedSiteVo.of(
                faviconToUse,
                voToSave.siteName(),
                voToSave.pageName(),
                normalizedUrl
        );
    }

    private boolean isFaviconValid(String faviconUrl) {
        return !Objects.isNull(faviconUrl) && !faviconUrl.isEmpty();
    }

    private void checkDuplicateAllowedSite(String normalizedUrl, Long allowedGroupId) {
        AllowedSite existingSite = fetchAllowedSiteService.fetchBySiteUrlAndAllowedGroupId(normalizedUrl, allowedGroupId);
        if (!Objects.isNull(existingSite)) {
            throw new DuplicateResourceException(ErrorMessage.DUPLICATE_RESOURCE);
        }
    }

    public void updateAllowedSiteUrl(Long allowedGroupId, Long allowedSiteId, AllowedSiteRequestDto allowedSiteRequestDto) {
        fetchAllowedSiteService.isExist(allowedSiteRequestDto.siteUrl(), allowedGroupId);
        AllowedSite findAllowedSite = fetchAllowedSiteService.fetchById(allowedSiteId);
        allowedSiteManager.updateAllowedSiteUrl(findAllowedSite, allowedSiteRequestDto.siteUrl());
    }

    @Transactional
    public void deleteAllowedSite(Long allowedSiteId) {
        deleteAllowedSiteService.delete(allowedSiteId);
    }

    @Transactional
    public void mergeToTopDomain(Long allowedGroupId, String siteUrl) {
        String topDomain = UrlUtils.getTopDomain(siteUrl);
        if (Objects.isNull(topDomain) || topDomain.isEmpty() || topDomain.equals("localhost")) return;
        List<AllowedSite> findAllowedSites = fetchAllowedSiteService.fetchByDomainContaining(allowedGroupId, topDomain);
        if (findAllowedSites.size() > 1) mergeToOne(topDomain, findAllowedSites);
    }

    public void mergeToOne(String topDomainUrl, List<AllowedSite> targetAllowedSites) {
        List<AllowedSite> sortedTargetAllowedSites = targetAllowedSites.stream().sorted(Comparator.comparing(AllowedSite::getSiteUrl)).toList();
        AllowedSite baseAllowedSite = sortedTargetAllowedSites.get(0);
        allowedSiteManager.updateAllowedSiteInfo(baseAllowedSite, fetchSiteInfoService.fetchSiteMetadataFromUrl(topDomainUrl));
        for (int i = 1; i < sortedTargetAllowedSites.size(); i++) {
            deleteAllowedSiteService.delete(sortedTargetAllowedSites.get(i).getId());
        }
    }

    public Map<InterestArea, List<RecommendSiteResponseDto>> fetchRecommendSitesOnboard() {
        return fetchRecommendSiteService.fetchAll().stream()
                .collect(Collectors.groupingBy(
                        RecommendSite::getInterestArea,
                        Collectors.mapping(RecommendSiteResponseDto::from, Collectors.toList())
                ));
    }

    @Transactional
    public void onboard(Long userId, String interestArea, OnboardRequestDto onboardRequestDto) {
        User findUser = fetchUserService.fetchByUserId(userId);
        userManager.completeOnboarding(findUser);
        // 온보딩 건너뛰기를 누른 경우 (쿼리 파라미터가 비어있는 경우 OR 요청 Body가 비어있는 경우)
        // interestArea를 OTHERS로 설정
        if (interestArea.equals("unknown") || onboardRequestDto == null) {
            userManager.updateUserInterestArea(findUser, InterestArea.OTHERS.getInterestArea());
            return;
        }
        // 온보딩을 정상적으로 진행하는 경우
        userManager.updateUserInterestArea(findUser, interestArea);
        AllowedGroup createdAllowedGroup = createAllowedGroupService.createWithBody(findUser, onboardRequestDto.name(), onboardRequestDto.colorCode());
        createAllowedSiteService.createAll(createdAllowedGroup, onboardRequestDto.allowedSiteVos());
    }

    public AllowedSiteVo fetchAllowedSiteInfoForOnBoard(String siteUrl) {
        String originalUrl = siteUrl;
        String normalizedUrl = UrlUtils.normalizeUrl(originalUrl);
        AllowedSiteVo voToSave;
        try {
            voToSave = determineAllowedSiteVoToSave(originalUrl, normalizedUrl);
            if (voToSave.siteName().isEmpty() || voToSave.pageName().isEmpty()) {
                return AllowedSiteVo.of(
                        voToSave.favicon().isEmpty() ? "" : voToSave.favicon(),
                        UrlUtils.getTopDomain(originalUrl),
                        UrlUtils.getTopDomain(originalUrl),
                        originalUrl
                );
            }
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateResourceException(ErrorMessage.DUPLICATE_RESOURCE);
        } catch (Exception e) {
            return AllowedSiteVo.of(
                    "",
                    UrlUtils.getTopDomain(originalUrl),
                    UrlUtils.getTopDomain(originalUrl),
                    originalUrl
            );
        }
        return voToSave;
    }
}
