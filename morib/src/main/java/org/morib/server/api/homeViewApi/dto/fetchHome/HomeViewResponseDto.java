package org.morib.server.api.homeViewApi.dto.fetchHome;

import lombok.Builder;
import org.morib.server.domain.category.infra.Category;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Builder
public class HomeViewResponseDto {
    LocalDate date;
    List<CombinedCategoryAndTaskForFetchHomeView> combinedCategoryAndTaskForFetchHomeViewList;

    public void init(LocalDate date) {
        this.date = date;
        this.combinedCategoryAndTaskForFetchHomeViewList = new ArrayList<>();
    }
}

