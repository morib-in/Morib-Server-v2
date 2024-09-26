package org.morib.server.api.homeView.dto.fetch;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.morib.server.api.homeView.vo.CombinedCategoryAndTaskInfo;

import java.time.LocalDate;
import java.util.List;

public record HomeViewResponseDto (
        LocalDate date,
        @JsonProperty("categories")
        List<CombinedCategoryAndTaskInfo> combinedCategoryAndTaskInfos
) {
    public static HomeViewResponseDto of(LocalDate date, List<CombinedCategoryAndTaskInfo> combinedCategoryAndTaskInfos) {
        return new HomeViewResponseDto(date, combinedCategoryAndTaskInfos);
    }
}

