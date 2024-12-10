package org.morib.server.domain.allowedGroup.application;

import org.morib.server.domain.allowedGroup.infra.AllowedGroup;

public interface FetchAllowedGroupService {
    AllowedGroup findById(Long groupId);
}
