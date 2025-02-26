package org.morib.server.api.allowGroupView.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.morib.server.api.allowGroupView.dto.AllowedSiteVo;
import org.morib.server.api.allowGroupView.dto.CreateAllowedSiteRequestDto;
import org.morib.server.api.allowGroupView.dto.UpdateAllowedGroupColorCodeRequestDto;
import org.morib.server.api.allowGroupView.dto.UpdateAllowedGroupNameRequestDto;
import org.morib.server.api.allowGroupView.facade.AllowedGroupViewFacade;
import org.morib.server.global.common.ApiResponseUtil;
import org.morib.server.global.common.BaseResponse;
import org.morib.server.global.common.ConnectType;
import org.morib.server.global.message.SuccessMessage;
import org.morib.server.global.userauth.CustomUserDetails;
import org.morib.server.global.userauth.PrincipalHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2")
public class AllowedGroupViewController {

    private final AllowedGroupViewFacade allowedGroupViewFacade;
    private final PrincipalHandler principalHandler;

    // 허용 서비스 세트 관련 api
    @PostMapping("/allowedGroup")
    public ResponseEntity<BaseResponse<?>> createAllowedGroup(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Long userId = principalHandler.getUserIdFromUserDetails(customUserDetails);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS, allowedGroupViewFacade.createAllowedGroup(userId));
    }

    @GetMapping("/allowedGroupList")
    public ResponseEntity<BaseResponse<?>> fetchAllowedGroupList(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                                 @RequestParam ConnectType connectType){
        Long userId = principalHandler.getUserIdFromUserDetails(customUserDetails);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS, allowedGroupViewFacade.fetchAllowedGroupList(userId, connectType));
    }

    @GetMapping("/allowedGroup/{allowedGroupId}")
    public ResponseEntity<BaseResponse<?>> fetchAllowedGroup(@PathVariable Long allowedGroupId,
                                                             @RequestParam ConnectType connectType){
        return ApiResponseUtil.success(SuccessMessage.SUCCESS, allowedGroupViewFacade.fetchAllowedGroup(allowedGroupId, connectType));
    }

    @PatchMapping("/allowedGroup/{allowedGroupId}/name")
    public ResponseEntity<BaseResponse<?>> updateAllowedGroupName(@PathVariable Long allowedGroupId,
                                                                  @Valid @RequestBody UpdateAllowedGroupNameRequestDto dto) {
        allowedGroupViewFacade.updateAllowedGroupName(allowedGroupId, dto);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS);
    }

    @PatchMapping("/allowedGroup/{allowedGroupId}/colorCode")
    public ResponseEntity<BaseResponse<?>> updateAllowedGroupColorCode(@PathVariable Long allowedGroupId,
                                                                       @Valid @RequestBody UpdateAllowedGroupColorCodeRequestDto dto) {
        allowedGroupViewFacade.updateAllowedGroupColorCode(allowedGroupId, dto);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS);
    }

    @DeleteMapping("/allowedGroup/{allowedGroupId}")
    public ResponseEntity<BaseResponse<?>> deleteAllowedGroup(@PathVariable Long allowedGroupId){
        allowedGroupViewFacade.deleteAllowedGroup(allowedGroupId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS);
    }

    // 허용 서비스 관련 api
    @PostMapping("/allowedSite/{allowedGroupId}")
    public ResponseEntity<BaseResponse<?>> createAllowedSite(@PathVariable Long allowedGroupId,
                                                             @Valid @RequestBody AllowedSiteRequestDto allowedSiteRequestDto) {
        allowedGroupViewFacade.createAllowedSite(allowedGroupId, createAllowedSiteRequestDto);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS);
    }
        return ApiResponseUtil.success(SuccessMessage.SUCCESS);
    }

    @GetMapping("/recommendSite")
    public ResponseEntity<BaseResponse<?>> fetchRecommendSites(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        Long userId = principalHandler.getUserIdFromUserDetails(customUserDetails);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS, allowedGroupViewFacade.fetchRecommendSites(userId));
    }

    @DeleteMapping("/allowedSite/{allowedSiteId}")
    public ResponseEntity<BaseResponse<?>> deleteAllowedSite(@PathVariable Long allowedSiteId) {
        allowedGroupViewFacade.deleteAllowedSite(allowedSiteId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS);
    }
  
    // 온보딩
    @PostMapping("/onboard")
    public ResponseEntity<BaseResponse<?>> onboard(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                   @RequestParam("interestArea") String interestArea,
                                                   @RequestBody OnboardRequestDto onboardRequestDto) {
        Long userId = principalHandler.getUserIdFromUserDetails(customUserDetails);
        allowedGroupViewFacade.onboard(userId, interestArea, onboardRequestDto);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS);
    }
}
