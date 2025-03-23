package org.morib.server.domain.allowedGroup.application.impl;

import lombok.RequiredArgsConstructor;
import org.morib.server.domain.allowedGroup.application.FetchAllowedGroupService;
import org.morib.server.domain.allowedGroup.infra.AllowedGroup;
import org.morib.server.domain.allowedGroup.infra.AllowedGroupRepository;
import org.morib.server.global.exception.NotFoundException;
import org.morib.server.global.message.ErrorMessage;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FetchAllowedGroupServiceImpl implements FetchAllowedGroupService {

    private final AllowedGroupRepository allowedGroupRepository;

    @Override
    public AllowedGroup findById(Long groupId) {
        return allowedGroupRepository.findById(groupId)
            .orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND));
    }

    @Override
    public List<AllowedGroup> findAllByUserId(Long userId) {
        return allowedGroupRepository.findAllByUserId(userId);
    }

    @Override
    public int getCounts(Long userId) {
        return allowedGroupRepository.countByUserId(userId);
    }
}
