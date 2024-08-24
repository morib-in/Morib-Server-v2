package org.morib.server.api.homeView.dto.fetch;

import java.util.List;

public record FetchCombinedDto(
        FetchCategoryDto category,
        List<FetchTaskDto> tasks
) {
    public static FetchCombinedDto of(FetchCategoryDto category, List<FetchTaskDto> tasks) {
        return new FetchCombinedDto(category, tasks);
    }


}

