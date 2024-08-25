package org.morib.server.api.modalView.dto;

import java.util.List;
import org.morib.server.api.modalView.vo.CategoryInfoInAllowedSite;
import org.morib.server.api.modalView.vo.MsetInfosInAllowedSite;

public record AllowedSiteByCategoryResponseDto(
    CategoryInfoInAllowedSite category,
    List<MsetInfosInAllowedSite> mSetList
) {
    public static AllowedSiteByCategoryResponseDto of(CategoryInfoInAllowedSite category, List<MsetInfosInAllowedSite> mSetList) {
        return new AllowedSiteByCategoryResponseDto(category, mSetList);
    }
}
