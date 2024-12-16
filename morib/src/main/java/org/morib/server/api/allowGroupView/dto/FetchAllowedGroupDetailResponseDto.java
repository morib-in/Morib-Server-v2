package org.morib.server.api.allowGroupView.dto;

import java.util.List;

public record FetchAllowedGroupDetailResponseDto(
    Long id,
    String name,
    List<FetchAllowedGroupDetailAllowedSiteVo> allowedSites
) {
   public static FetchAllowedGroupDetailResponseDto of(Long id, String name, List<FetchAllowedGroupDetailAllowedSiteVo> allowedSitesVo){
         return new FetchAllowedGroupDetailResponseDto(id, name, allowedSitesVo);
   }

}
