package org.morib.server.domain.recentAllowedGroup.application.impl;

import lombok.RequiredArgsConstructor;
import org.morib.server.domain.recentAllowedGroup.application.CreateRecentAllowedGroupService;
import org.morib.server.domain.recentAllowedGroup.infra.RecentAllowedGroup;
import org.morib.server.domain.recentAllowedGroup.infra.RecentAllowedGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CreateRecentAllowedGroupServiceImpl implements CreateRecentAllowedGroupService {

    private final RecentAllowedGroupRepository recentAllowedGroupRepository;

    @Override
    @Transactional
    public void create(List<RecentAllowedGroup> recentAllowedGroupList) {
        recentAllowedGroupRepository.saveAll(recentAllowedGroupList);
    }
}
