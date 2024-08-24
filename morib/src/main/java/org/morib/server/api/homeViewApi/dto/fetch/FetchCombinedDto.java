package org.morib.server.api.homeViewApi.dto.fetch;

import org.morib.server.domain.category.infra.Category;
import org.morib.server.domain.task.infra.Task;
import org.morib.server.domain.timer.infra.Timer;

import java.util.List;
import java.util.Map;

public record FetchCombinedDto(
        FetchCategoryDto category,
        List<FetchTaskDto> tasks
) {
    public static FetchCombinedDto of(FetchCategoryDto category, List<FetchTaskDto> tasks) {
        return new FetchCombinedDto(category, tasks);
    }


}

