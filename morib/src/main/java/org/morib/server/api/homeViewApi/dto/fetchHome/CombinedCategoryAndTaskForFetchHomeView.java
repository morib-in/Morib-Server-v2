package org.morib.server.api.homeViewApi.dto.fetchHome;

import java.util.List;

public record CombinedCategoryAndTaskForFetchHomeView(
        CategoryForFetchHomeView categoryForFetchHomeView,
        List<TaskWithTimerForFetchHomeView> taskWithTimerForFetchHomeViewList
) {
    public void addToTaskWithTimerForFetchHomeView(TaskWithTimerForFetchHomeView taskWithTimerForFetchHomeView) {
        this.taskWithTimerForFetchHomeViewList.add(taskWithTimerForFetchHomeView);
    }

    public static CombinedCategoryAndTaskForFetchHomeView of(CategoryForFetchHomeView categoryForFetchHomeView, List<TaskWithTimerForFetchHomeView> taskWithTimerForFetchHomeViewList) {
        return new CombinedCategoryAndTaskForFetchHomeView(categoryForFetchHomeView, taskWithTimerForFetchHomeViewList);
    }
}

