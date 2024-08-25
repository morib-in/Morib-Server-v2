package org.morib.server.api.modalView.vo;

import java.time.LocalDateTime;
import org.morib.server.domain.allowedSite.infra.AllowedSite;

public record MsetInfosInAllowedSite(
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    Long id,
    String name,
    String url
) {
    public static MsetInfosInAllowedSite of(AllowedSite allowedSite) {
        return new MsetInfosInAllowedSite(allowedSite.getCreatedAt(),
            allowedSite.getUpdatedAt(), allowedSite.getId(), allowedSite.getSiteName(),
            allowedSite.getSiteUrl());
    }
}
