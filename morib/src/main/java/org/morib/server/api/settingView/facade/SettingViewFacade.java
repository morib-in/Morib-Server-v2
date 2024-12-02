package org.morib.server.api.settingView.facade;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.morib.server.annotation.Facade;
import org.morib.server.api.settingView.dto.FetchUserProfileResponseDto;
import org.morib.server.api.settingView.dto.UpdateUserProfileRequestDto;
import org.morib.server.domain.user.UserManager;
import org.morib.server.domain.user.application.FetchUserService;
import org.morib.server.domain.user.infra.User;

@RequiredArgsConstructor
@Facade
public class SettingViewFacade {
    private final FetchUserService fetchUserService;
    private final UserManager userManager;

    public FetchUserProfileResponseDto fetchUserProfile(Long userId) {
        User findUser = fetchUserService.fetchByUserId(userId);
        return FetchUserProfileResponseDto.of(findUser.getId(), findUser.getName(), findUser.getEmail(), findUser.getImageUrl(), findUser.isPushEnabled());
    }

    @Transactional
    public void updateUserProfile(Long userId, UpdateUserProfileRequestDto updateUserProfileRequestDto) {
        User findUser = fetchUserService.fetchByUserId(userId);
        userManager.updateUserProfile(findUser, updateUserProfileRequestDto.name(), updateUserProfileRequestDto.imageUrl(), updateUserProfileRequestDto.isPushEnabled());
    }

}
