package org.morib.server.api.allowGroupView.dto;

import jakarta.validation.constraints.NotBlank;
import org.morib.server.annotation.ValidUrl;

public record AllowedSiteRequestDto(
        @NotBlank
        @ValidUrl
        String siteUrl
){
    public static AllowedSiteRequestDto of(String siteUrl){
        return new AllowedSiteRequestDto(siteUrl);
    }

}
