package org.morib.server.api.allowGroupView.controller;

import lombok.RequiredArgsConstructor;
import org.morib.server.api.allowGroupView.dto.AddAllowSiteInAllowGroupRequestDto;
import org.morib.server.api.allowGroupView.facade.AllowedGroupViewFacade;
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
@RequestMapping("/api/v2/allowServiceSet")
public class AllowedGroupViewController {

    private final AllowedGroupViewFacade allowedGroupViewFacade;

    @DeleteMapping("/{groupId}")
    public ResponseEntity<BaseResponse<?>> deleteAllowServiceSet(@PathVariable Long groupId){
        allowedGroupViewFacade.deleteAllowServiceSet(groupId);
        return ResponseEntity.ok(BaseResponse.of(SuccessMessage.SUCCESS));
    }

    @PostMapping("/allowedSite")
    public ResponseEntity<BaseResponse<?>> addAllowedSite(@RequestBody
        AddAllowSiteInAllowGroupRequestDto addAllowSiteInAllowGroupRequestDto){
        allowedGroupViewFacade.addAllowedSite(addAllowSiteInAllowGroupRequestDto);
        return ResponseEntity.ok(BaseResponse.of(SuccessMessage.SUCCESS));
    }


}
