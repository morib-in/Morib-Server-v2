package org.morib.server.api.allowGroupView.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record FetchAllowedGroupResponseDto(
    Long id,
    String name,
    String colorCode,
    @JsonProperty("allowedSites")
    List<AllowedSiteVo> allowedSiteVos
) {
   public static FetchAllowedGroupResponseDto of(Long id, String name, String colorCode, List<AllowedSiteVo> allowedSiteVos){
         return new FetchAllowedGroupResponseDto(id, name, colorCode, allowedSiteVos);
   }

}
