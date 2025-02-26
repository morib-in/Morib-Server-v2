package org.morib.server.domain.recentAllowedGroup.application.impl;

import lombok.RequiredArgsConstructor;
import org.morib.server.domain.recentAllowedGroup.application.FetchRecentAllowedGroupService;
import org.morib.server.domain.recentAllowedGroup.infra.RecentAllowedGroup;
import org.morib.server.domain.recentAllowedGroup.infra.RecentAllowedGroupRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FetchRecentAllowedGroupServiceImpl implements FetchRecentAllowedGroupService {

    private final RecentAllowedGroupRepository recentAllowedGroupRepository;

    @Override
    public List<RecentAllowedGroup> findAllByUserId(Long userId) {
        return recentAllowedGroupRepository.findAllByUserId(userId);
    }
}
