package org.morib.server.api.modalView.dto;

import java.util.List;
import org.morib.server.api.modalView.vo.CategoryInfoInAllowedSite;
import org.morib.server.api.modalView.vo.AllowSiteForCalledByCatgory;

public record AllowedSiteByCategoryResponseDto(
    CategoryInfoInAllowedSite category,
    List<AllowSiteForCalledByCatgory> msetList
) {
    public static AllowedSiteByCategoryResponseDto of(CategoryInfoInAllowedSite category, List<AllowSiteForCalledByCatgory> msetList) {
        return new AllowedSiteByCategoryResponseDto(category, msetList);
    }
}
