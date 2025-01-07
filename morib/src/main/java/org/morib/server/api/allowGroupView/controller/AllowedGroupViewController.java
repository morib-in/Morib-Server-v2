package org.morib.server.api.allowGroupView.controller;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.morib.server.api.allowGroupView.dto.CreateAllowedSiteInAllowedGroupRequestDto;
import org.morib.server.api.allowGroupView.dto.UpdateAllowedGroupColorCodeRequestDto;
import org.morib.server.api.allowGroupView.dto.UpdateAllowedGroupNameRequestDto;
import org.morib.server.api.allowGroupView.facade.AllowedGroupViewFacade;
import org.morib.server.global.common.ConnectType;
import org.morib.server.global.common.ApiResponseUtil;
import org.morib.server.global.common.BaseResponse;
import org.morib.server.global.exception.InvalidQueryParameterException;
import org.morib.server.global.message.ErrorMessage;
import org.morib.server.global.message.SuccessMessage;
import org.morib.server.global.userauth.CustomUserDetails;
import org.morib.server.global.userauth.PrincipalHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/allowedServiceSet")
public class AllowedGroupViewController {

    private final AllowedGroupViewFacade allowedGroupViewFacade;
    private final PrincipalHandler principalHandler;

    @DeleteMapping("/{groupId}")
    public ResponseEntity<BaseResponse<?>> deleteAllowedServiceSet(@PathVariable Long groupId){
        allowedGroupViewFacade.deleteAllowedServiceSet(groupId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS);
    }

    @DeleteMapping("/allowedSite/{allowedSiteId}")
    public ResponseEntity<BaseResponse<?>> deleteAllowedSite(@PathVariable Long allowedSiteId) {
        allowedGroupViewFacade.deleteAllowedSite(allowedSiteId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS);
    }

    @PostMapping("/allowedSite")
    public ResponseEntity<BaseResponse<?>> addAllowedSite(@RequestBody
    CreateAllowedSiteInAllowedGroupRequestDto createAllowedSiteInAllowedGroupRequestDto) {
        allowedGroupViewFacade.addAllowedSite(createAllowedSiteInAllowedGroupRequestDto);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS);
    }


    @PatchMapping("/{groupId}/name")
    public ResponseEntity<BaseResponse<?>> updateAllowedGroupName(@PathVariable Long groupId,
        @RequestBody
        UpdateAllowedGroupNameRequestDto dto) {
        if (Objects.isNull(dto))
            throw new InvalidQueryParameterException(ErrorMessage.BAD_REQUEST);

        allowedGroupViewFacade.updateAllowedGroupName(groupId,dto);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS);
    }

    @PatchMapping("/{groupId}/colorCode")
    public ResponseEntity<BaseResponse<?>> updateAllowedGroupColorCode(@PathVariable Long groupId,
        @RequestBody UpdateAllowedGroupColorCodeRequestDto dto) {
        if (Objects.isNull(dto))
            throw new InvalidQueryParameterException(ErrorMessage.BAD_REQUEST);

        allowedGroupViewFacade.updateAllowedGroupColorCode(groupId,dto);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS);
    }

    @GetMapping
    public ResponseEntity<BaseResponse<?>> getAllowedGroups(@AuthenticationPrincipal CustomUserDetails userDetails,  @RequestParam ConnectType connectType){
        Long userId = principalHandler.getUserIdFromUserDetails(userDetails);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS, allowedGroupViewFacade.getAllowedGroups(userId, connectType));
    }

    @GetMapping("/{groupId}")
    public ResponseEntity<BaseResponse<?>> getAllowedGroups(@PathVariable Long groupId, @RequestParam ConnectType connectType){
        return ApiResponseUtil.success(SuccessMessage.SUCCESS, allowedGroupViewFacade.getGroupDetail(groupId, connectType));
    }

    @GetMapping("/recommendSite")
    public ResponseEntity<BaseResponse<?>> getRecommendSite(@AuthenticationPrincipal CustomUserDetails userDetails){
        return ApiResponseUtil.success(SuccessMessage.SUCCESS, allowedGroupViewFacade.getRecommendSite(userDetails.getUserId()));
    }


}
