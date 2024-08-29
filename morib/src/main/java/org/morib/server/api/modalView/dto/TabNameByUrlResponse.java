package org.morib.server.api.modalView.dto;

public record TabNameByUrlResponse(
        String tabName
) {
    public static TabNameByUrlResponse of(String tabName) {
        return new TabNameByUrlResponse(tabName);
    }
}
