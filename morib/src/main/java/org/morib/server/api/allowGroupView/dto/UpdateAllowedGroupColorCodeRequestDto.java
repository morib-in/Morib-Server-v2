package org.morib.server.api.allowGroupView.dto;

public record UpdateAllowedGroupColorCodeRequestDto(String colorCode) {

    public static UpdateAllowedGroupColorCodeRequestDto of(String colorCode) {
        return new UpdateAllowedGroupColorCodeRequestDto(colorCode);
    }

}
