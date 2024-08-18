package org.morib.server.domain.user.application;

import lombok.RequiredArgsConstructor;
import org.morib.server.domain.mset.infra.MsetRepository;
import org.morib.server.domain.user.infra.UserRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserDataServiceImpl implements UserDataService {

    private final UserRepository userRepository;

    @Override
    public void save() {

    }

    @Override
    public void findById() {

    }

    @Override
    public void findAll() {

    }

    @Override
    public void deleteById() {

    }

    @Override
    public void deleteAll() {

    }
}
