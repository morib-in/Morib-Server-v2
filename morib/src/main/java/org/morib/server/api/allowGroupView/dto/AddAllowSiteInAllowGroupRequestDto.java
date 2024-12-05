package org.morib.server.api.allowGroupView.dto;

public record AddAllowSiteInAllowGroupRequestDto(
    Long groupId,
    String allowedSiteUrl){

    public static AddAllowSiteInAllowGroupRequestDto of(Long groupId, String allowedSiteUrl){
        return new AddAllowSiteInAllowGroupRequestDto(groupId, allowedSiteUrl);
    }

}
