package org.morib.server.domain.allowedGroup.application.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.morib.server.domain.allowedGroup.application.FetchAllowedGroupService;
import org.morib.server.domain.allowedGroup.infra.AllowedGroup;
import org.morib.server.domain.allowedGroup.infra.AllowedGroupRepository;
import org.morib.server.global.exception.NotFoundException;
import org.morib.server.global.message.ErrorMessage;
import org.springframework.stereotype.Service;

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


}
