package org.morib.server.api.modalView.vo;

import org.morib.server.domain.allowedSite.infra.AllowedSite;

// imageUrl 없다면 -> calledByOwnerType 으로 변경하여 진행하기
public record AllowSiteForCalledByTask(
    Long id,
    String name,
    String url
) {
    public static AllowSiteForCalledByTask of(AllowedSite allowedSite) { // 다른 api의 공통된 부분을 찾아서 추후에 수정
        return new AllowSiteForCalledByTask(allowedSite.getId(), allowedSite.getSiteName(),
            allowedSite.getSiteUrl());
    }
}
