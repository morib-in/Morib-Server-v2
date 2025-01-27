package org.morib.server.global.sse.api;

public record UserInfoDtoForSseUserInfoWrapper(
        Long id,
        int elapsedTime,
        String runningCategoryName,
        Long taskId
) {
    public static UserInfoDtoForSseUserInfoWrapper of(Long id, int elapsedTime, String runningCategoryName, Long taskId) {
        return new UserInfoDtoForSseUserInfoWrapper(id, elapsedTime, runningCategoryName, taskId);
    }
}
