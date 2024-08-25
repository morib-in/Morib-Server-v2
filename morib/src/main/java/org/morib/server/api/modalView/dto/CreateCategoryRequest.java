package org.morib.server.api.modalView.dto;

import org.morib.server.domain.allowedSite.infra.AllowedSite;

import java.time.LocalDate;
import java.util.List;

public record CreateCategoryRequest(
        String name,
        LocalDate startDate,
        LocalDate endDate,
        List<AllowedSite> allowedSites
) {

}
