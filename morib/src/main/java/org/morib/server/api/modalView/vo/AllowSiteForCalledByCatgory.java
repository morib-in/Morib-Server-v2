package org.morib.server.api.modalView.vo;

import org.morib.server.domain.allowedSite.infra.AllowedSite;

public record AllowSiteForCalledByCatgory(
    Long id,
    String name,
    String url
) {
    public static AllowSiteForCalledByCatgory of(AllowedSite allowedSite) { // 다른 api의 공통된 부분을 찾아서 추후에 수정
        return new AllowSiteForCalledByCatgory(allowedSite.getId(), allowedSite.getSiteName(),
            allowedSite.getSiteUrl());
    }
}
