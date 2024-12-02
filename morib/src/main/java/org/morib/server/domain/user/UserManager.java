package org.morib.server.domain.user;

import org.morib.server.annotation.Manager;
import org.morib.server.domain.user.infra.User;

@Manager
public class UserManager {

    public void updateUserProfile(User findUser, String name, String imageUrl, boolean isPushEnabled) {
        findUser.updateProfile(name, imageUrl, isPushEnabled);
    }

}
