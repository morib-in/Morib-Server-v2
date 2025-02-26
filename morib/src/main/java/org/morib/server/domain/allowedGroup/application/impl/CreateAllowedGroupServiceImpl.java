package org.morib.server.domain.allowedGroup.application.impl;

import lombok.RequiredArgsConstructor;
import org.morib.server.domain.allowedGroup.application.CreateAllowedGroupService;
import org.morib.server.domain.allowedGroup.infra.AllowedGroup;
import org.morib.server.domain.allowedGroup.infra.AllowedGroupRepository;
import org.morib.server.domain.user.infra.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CreateAllowedGroupServiceImpl implements CreateAllowedGroupService {

    private final AllowedGroupRepository allowedGroupRepository;

    @Transactional
    @Override
    public AllowedGroup create(User user, int nameIdx) {
        return allowedGroupRepository.saveAndFlush(AllowedGroup.create(user, nameIdx));
    }

    @Transactional
    @Override
    public AllowedGroup createWithBody(User user, String name, String colorCode) {
        return allowedGroupRepository.save(AllowedGroup.createWithBody(user, name, colorCode));
    }
}
