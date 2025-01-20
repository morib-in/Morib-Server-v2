package org.morib.server.api.allowGroupView.dto;

import jakarta.validation.constraints.NotBlank;
import org.morib.server.annotation.ValidUrl;

public record CreateAllowedSiteRequestDto(
        @NotBlank
        @ValidUrl
        String siteUrl
){
    public static CreateAllowedSiteRequestDto of(String siteUrl){
        return new CreateAllowedSiteRequestDto(siteUrl);
    }

}
