package org.morib.server.api.allowGroupView.controller;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.morib.server.api.allowGroupView.dto.AddAllowSiteInAllowGroupRequestDto;
import org.morib.server.api.allowGroupView.facade.AllowedGroupViewFacade;
import org.morib.server.global.common.BaseResponse;
import org.morib.server.global.exception.InvalidQueryParameterException;
import org.morib.server.global.message.ErrorMessage;
import org.morib.server.global.message.SuccessMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/allowServiceSet")
public class AllowedGroupViewController {

    private final AllowedGroupViewFacade allowedGroupViewFacade;

    @DeleteMapping("/{groupId}")
    public ResponseEntity<BaseResponse<?>> deleteAllowServiceSet(@PathVariable Long groupId) {
        allowedGroupViewFacade.deleteAllowServiceSet(groupId);
        return ResponseEntity.ok(BaseResponse.of(SuccessMessage.SUCCESS));
    }

    @PostMapping("/allowedSite")
    public ResponseEntity<BaseResponse<?>> addAllowedSite(@RequestBody
    AddAllowSiteInAllowGroupRequestDto addAllowSiteInAllowGroupRequestDto) {
        allowedGroupViewFacade.addAllowedSite(addAllowSiteInAllowGroupRequestDto);
        return ResponseEntity.ok(BaseResponse.of(SuccessMessage.SUCCESS));
    }

    @PatchMapping("/{groupId}")
    public ResponseEntity<BaseResponse<?>> updateAllowedGroup(@PathVariable Long groupId,
        @RequestParam(required = false) String colorCode,
        @RequestParam(required = false) String name) {
        if (isParamAllNull(colorCode, name))
            throw new InvalidQueryParameterException(ErrorMessage.BAD_REQUEST);

        allowedGroupViewFacade.updateAllowedGroup(groupId, colorCode, name);
        return ResponseEntity.ok(BaseResponse.of(SuccessMessage.SUCCESS));
    }

    private boolean isParamAllNull(String colorCode, String name) {
        return Objects.isNull(colorCode) && Objects.isNull(name);
    }

}
