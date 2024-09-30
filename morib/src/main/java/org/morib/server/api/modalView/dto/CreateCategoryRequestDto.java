package org.morib.server.api.modalView.dto;

import org.morib.server.api.modalView.vo.AllowedSiteInfo;
import org.morib.server.domain.allowedSite.infra.AllowedSite;

import java.time.LocalDate;
import java.util.List;

public record CreateCategoryRequestDto(
        String name,
        List<AllowedSiteInfo> msets
) {
}
