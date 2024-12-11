package org.morib.server.domain.allowedGroup.application;

import java.util.List;
import org.morib.server.domain.allowedGroup.infra.AllowedGroup;

public interface FetchAllowedGroupService {
    AllowedGroup findById(Long groupId);

    List<AllowedGroup> findAllByUserId(Long userId);
}
