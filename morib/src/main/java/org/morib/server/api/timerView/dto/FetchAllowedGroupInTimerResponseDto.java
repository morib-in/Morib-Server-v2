package org.morib.server.api.timerView.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.morib.server.api.allowGroupView.dto.AllowedSiteWithIdVo;

import java.util.List;

public record FetchAllowedGroupInTimerResponseDto(
        Long id,
        String name,
        String colorCode,
        boolean selected,
        @JsonProperty("allowedSites")
        List<AllowedSiteWithIdVo> allowedSiteVos
) {
    public static FetchAllowedGroupInTimerResponseDto of(Long id, String name, String colorCode, boolean selected, List<AllowedSiteWithIdVo> allowedSiteWithIdVos){
        return new FetchAllowedGroupInTimerResponseDto(id, name, colorCode, selected, allowedSiteWithIdVos);
    }

}
