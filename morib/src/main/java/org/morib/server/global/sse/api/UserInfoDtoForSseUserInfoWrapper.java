package org.morib.server.global.sse.api;

public record UserInfoDtoForSseUserInfoWrapper(
        Long userId,
        int elapsedTime,
        String runningCategoryName,
        Long taskId
) {
    public static UserInfoDtoForSseUserInfoWrapper of(Long userId, int elapsedTime, String runningCategoryName, Long taskId) {
        return new UserInfoDtoForSseUserInfoWrapper(userId, elapsedTime, runningCategoryName, taskId);
    }
}
