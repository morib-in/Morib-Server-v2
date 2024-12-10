package org.morib.server.api.allowGroupView.dto;

public record CreateAllowedSiteInAllowedGroupRequestDto(
    Long groupId,
    String allowedSiteUrl){

    public static CreateAllowedSiteInAllowedGroupRequestDto of(Long groupId, String allowedSiteUrl){
        return new CreateAllowedSiteInAllowedGroupRequestDto(groupId, allowedSiteUrl);
    }

}
