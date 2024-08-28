package org.morib.server.api.modalView.vo;

import org.morib.server.domain.allowedSite.infra.AllowedSite;

public record AllowSiteForCalledByTask(
    Long id,
    String name,
    String url
) {
    public static AllowSiteForCalledByTask of(AllowedSite allowedSite) {
        return new AllowSiteForCalledByTask(allowedSite.getId(), allowedSite.getSiteName(),
            allowedSite.getSiteUrl());
    }
}
