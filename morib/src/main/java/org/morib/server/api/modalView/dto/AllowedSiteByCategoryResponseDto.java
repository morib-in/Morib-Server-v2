package org.morib.server.api.modalView.dto;

import java.util.List;
import org.morib.server.api.modalView.vo.CategoryInfoInAllowedSite;
import org.morib.server.api.modalView.vo.AllowSiteForCalledByCatgory;

public record AllowedSiteByCategoryResponseDto(
    CategoryInfoInAllowedSite category,
    List<AllowSiteForCalledByCatgory> mSetList
) {
    public static AllowedSiteByCategoryResponseDto of(CategoryInfoInAllowedSite category, List<AllowSiteForCalledByCatgory> mSetList) {
        return new AllowedSiteByCategoryResponseDto(category, mSetList);
    }
}
