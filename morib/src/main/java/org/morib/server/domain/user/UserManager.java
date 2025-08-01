package org.morib.server.domain.user;

import java.util.Objects;

import org.morib.server.annotation.Manager;
import org.morib.server.domain.user.application.dto.UpdateUserProfileServiceDto;
import org.morib.server.domain.user.infra.User;
import org.morib.server.domain.user.infra.type.InterestArea;

import lombok.extern.slf4j.Slf4j;

@Slf4j
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

    public void updateUserInterestArea(User findUser, String interestArea) {
        findUser.updateUserInterestArea(InterestArea.fromValue(interestArea));
    }

    public void completeOnboarding(User findUser) {
        findUser.completeOnboarding();
    }


    public void updateUserName(User findUser, String fullName) {
        if(Objects.isNull(fullName) || findUser.isOnboardingCompleted()){
            log.info("Full name is null or is already onboarding");
            return;
        }
        log.info("Full name was {}", fullName);
        findUser.updateUserName(fullName);
    }
}
