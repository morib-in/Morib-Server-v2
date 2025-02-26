package org.morib.server.domain.recentAllowedGroup.application;

import org.morib.server.domain.recentAllowedGroup.infra.RecentAllowedGroup;

import java.util.List;

public interface FetchRecentAllowedGroupService {
    List<RecentAllowedGroup> findAllByUserId(Long userId);
}
