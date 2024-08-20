package org.morib.server.domain.relationship.application;

import lombok.RequiredArgsConstructor;
import org.morib.server.domain.relationship.infra.RelationshipRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RelationshipGatewayImpl implements RelationshipGateway {

    private final RelationshipRepository permissionRepository;

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
