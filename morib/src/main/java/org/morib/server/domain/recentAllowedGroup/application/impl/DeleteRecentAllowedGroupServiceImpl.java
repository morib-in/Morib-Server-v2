package org.morib.server.domain.recentAllowedGroup.application.impl;

import lombok.RequiredArgsConstructor;
import org.morib.server.domain.recentAllowedGroup.application.DeleteRecentAllowedGroupService;
import org.morib.server.domain.recentAllowedGroup.infra.RecentAllowedGroup;
import org.morib.server.domain.recentAllowedGroup.infra.RecentAllowedGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeleteRecentAllowedGroupServiceImpl implements DeleteRecentAllowedGroupService {

    private final RecentAllowedGroupRepository recentAllowedGroupRepository;

    @Override
    @Transactional
    public void deleteAll(Long userId) {
        recentAllowedGroupRepository.deleteAllByUserId(userId);
    }

    @Override
    @Transactional
    public void delete(List<RecentAllowedGroup> recentAllowedGroupList) {
        recentAllowedGroupRepository.deleteAll(recentAllowedGroupList);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        recentAllowedGroupRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteByAllowedGroupId(Long allowedGroupId) {
        recentAllowedGroupRepository.deleteBySelectedAllowedGroupId(allowedGroupId);
    }
}
