package org.morib.server.api.homeView.vo;

import java.util.Collections;
import java.util.List;

public record CombinedCategoryAndTaskInfo(
        CategoryInfo category,
        List<TaskInfo> tasks
) {
    public static CombinedCategoryAndTaskInfo of(CategoryInfo category, List<TaskInfo> tasks) {
        return new CombinedCategoryAndTaskInfo(category, tasks);
    }


}

