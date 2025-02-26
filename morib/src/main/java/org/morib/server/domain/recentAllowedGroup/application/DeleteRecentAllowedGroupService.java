package org.morib.server.domain.recentAllowedGroup.application;

import org.morib.server.domain.recentAllowedGroup.infra.RecentAllowedGroup;

import java.util.List;

public interface DeleteRecentAllowedGroupService {
    void deleteAll(Long userId);
    void delete(List<RecentAllowedGroup> recentAllowedGroupList);
    void deleteById(Long id);
    void deleteByAllowedGroupId(Long allowedGroupId);
}
