package org.morib.server.api.allowGroupView.dto;

import java.util.List;
import org.morib.server.domain.user.infra.type.InterestAreaSiteVo;

public record InterestAreaSiteResponseDto(
        List<InterestAreaSiteVo> recommendSites
) {
    public static InterestAreaSiteResponseDto of(List<InterestAreaSiteVo> interestAreaSiteVos) {
        return new InterestAreaSiteResponseDto(interestAreaSiteVos);
    }
}
