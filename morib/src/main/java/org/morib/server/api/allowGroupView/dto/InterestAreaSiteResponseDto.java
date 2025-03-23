package org.morib.server.api.allowGroupView.dto;

import org.morib.server.domain.user.infra.type.InterestAreaSiteVo;

import java.util.List;

public record InterestAreaSiteResponseDto(
        List<InterestAreaSiteVo> recommendSites
) {
    public static InterestAreaSiteResponseDto of(List<InterestAreaSiteVo> interestAreaSiteVos) {
        return new InterestAreaSiteResponseDto(interestAreaSiteVos);
    }
}
