package org.morib.server.domain.allowedGroup.application;

import org.morib.server.domain.allowedGroup.infra.AllowedGroup;

import java.util.List;

public interface FetchAllowedGroupService {
    AllowedGroup findById(Long groupId);
    List<AllowedGroup> findAllByUserId(Long userId);
    int getCounts(Long userId);
}
