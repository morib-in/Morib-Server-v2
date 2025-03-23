package org.morib.server.api.modalView.dto;

import org.morib.server.api.modalView.vo.AllowSiteForCalledByCatgory;
import org.morib.server.api.modalView.vo.CategoryInfoInAllowedSite;

import java.util.List;

public record AllowedSiteByCategoryResponseDto(
    CategoryInfoInAllowedSite category,
    List<AllowSiteForCalledByCatgory> msetList
) {
    public static AllowedSiteByCategoryResponseDto of(CategoryInfoInAllowedSite category, List<AllowSiteForCalledByCatgory> msetList) {
        return new AllowedSiteByCategoryResponseDto(category, msetList);
    }
}
