package org.morib.server.api.allowGroupView.dto;

import java.util.List;

public record FetchAllowedGroupDetailResponseDto(
    Long id,
    String name,
    List<AllowedSiteVo> allowedSites
) {
   public static FetchAllowedGroupDetailResponseDto of(Long id, String name, List<AllowedSiteVo> allowedSitesVo){
         return new FetchAllowedGroupDetailResponseDto(id, name, allowedSitesVo);
   }

}
