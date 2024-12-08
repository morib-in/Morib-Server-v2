package org.morib.server.domain.allowedGroup.application;

import org.morib.server.domain.allowedGroup.infra.AllowedGroup;

public interface AllowedGroupService {
    void deleteAllowedGroupById(Long groupId);

    AllowedGroup findById(Long groupId);
}
