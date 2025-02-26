package org.morib.server.api.allowGroupView.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record OnboardRequestDto(
        String name,
        String colorCode,
        @JsonProperty("allowedSites")
        List<AllowedSiteVo> allowedSiteVos
) {
}
