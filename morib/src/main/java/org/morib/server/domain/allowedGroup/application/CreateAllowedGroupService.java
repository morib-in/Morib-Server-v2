package org.morib.server.domain.allowedGroup.application;

import org.morib.server.domain.allowedGroup.infra.AllowedGroup;
import org.morib.server.domain.user.infra.User;

public interface CreateAllowedGroupService {
    AllowedGroup create(User user, int nameIdx);
}
