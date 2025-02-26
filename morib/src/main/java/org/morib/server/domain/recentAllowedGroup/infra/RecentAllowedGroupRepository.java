package org.morib.server.domain.recentAllowedGroup.infra;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecentAllowedGroupRepository extends JpaRepository<RecentAllowedGroup, Long> {
    List<RecentAllowedGroup> findAllByUserId(Long userId);
    void deleteAllByUserId(Long userId);
    void deleteBySelectedAllowedGroupId(Long allowedGroupId);
}
