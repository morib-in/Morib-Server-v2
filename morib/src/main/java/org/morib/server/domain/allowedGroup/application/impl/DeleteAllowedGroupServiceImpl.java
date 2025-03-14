package org.morib.server.domain.allowedGroup.application.impl;

import lombok.RequiredArgsConstructor;
import org.morib.server.domain.allowedGroup.application.DeleteAllowedGroupService;
import org.morib.server.domain.allowedGroup.infra.AllowedGroup;
import org.morib.server.domain.allowedGroup.infra.AllowedGroupRepository;
import org.morib.server.global.exception.NotFoundException;
import org.morib.server.global.message.ErrorMessage;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteAllowedGroupServiceImpl implements DeleteAllowedGroupService {

    private final AllowedGroupRepository allowedGroupRepository;

    @Override
    public void deleteAllowedGroupById(Long groupId) {
        AllowedGroup findAllowedGroup = allowedGroupRepository.findById(groupId)
            .orElseThrow(() -> new NotFoundException(
                ErrorMessage.NOT_FOUND));
        allowedGroupRepository.delete(findAllowedGroup);
    }
}
