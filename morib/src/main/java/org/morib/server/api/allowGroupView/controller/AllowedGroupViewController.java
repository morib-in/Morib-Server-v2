package org.morib.server.api.allowGroupView.controller;

import lombok.RequiredArgsConstructor;
import org.morib.server.api.allowGroupView.dto.CreateAllowedSiteInAllowedGroupRequestDto;
import org.morib.server.api.allowGroupView.facade.AllowedGroupViewFacade;
import org.morib.server.global.common.ApiResponseUtil;
import org.morib.server.global.common.BaseResponse;
import org.morib.server.global.message.SuccessMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/allowedServiceSet")
public class AllowedGroupViewController {

    private final AllowedGroupViewFacade allowedGroupViewFacade;

    @DeleteMapping("/{groupId}")
    public ResponseEntity<BaseResponse<?>> deleteAllowedServiceSet(@PathVariable Long groupId){
        allowedGroupViewFacade.deleteAllowedServiceSet(groupId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS);
    }

    @PostMapping("/allowedSite")
    public ResponseEntity<BaseResponse<?>> addAllowedSite(@RequestBody
    CreateAllowedSiteInAllowedGroupRequestDto createAllowedSiteInAllowedGroupRequestDto){
        allowedGroupViewFacade.addAllowedSite(createAllowedSiteInAllowedGroupRequestDto);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS);
    }


}
