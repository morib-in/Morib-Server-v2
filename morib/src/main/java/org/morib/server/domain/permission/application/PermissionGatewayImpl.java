package org.morib.server.domain.permission.application;

import lombok.RequiredArgsConstructor;
import org.morib.server.domain.permission.infra.PermissionRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PermissionGatewayImpl implements PermissionGateway {

    private final PermissionRepository permissionRepository;

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
