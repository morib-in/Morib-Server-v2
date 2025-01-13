package org.morib.server.domain.user;

import org.morib.server.annotation.Manager;
import org.morib.server.domain.user.application.dto.UpdateUserProfileServiceDto;
import org.morib.server.domain.user.infra.User;

@Manager
public class UserManager {

    public void updateUserProfile(User findUser, UpdateUserProfileServiceDto updateUserProfileServiceDto) {
        findUser.updateProfile(updateUserProfileServiceDto.name(), updateUserProfileServiceDto.imageUrl(), updateUserProfileServiceDto.isPushEnabled());
    }

    public void updateSocialRefreshToken(User findUser, String socialRefreshToken) {
        findUser.updateSocialRefreshToken(socialRefreshToken);
    }

    public void invalidateRefreshToken(User findUser) {
        findUser.invalidateRefreshToken();
    }


}
