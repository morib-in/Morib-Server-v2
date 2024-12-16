package org.morib.server.domain.relationship.application;

import lombok.RequiredArgsConstructor;
import org.morib.server.domain.relationship.infra.Relationship;
import org.morib.server.domain.relationship.infra.RelationshipRepository;
import org.morib.server.domain.user.infra.User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateRelationshipServiceImpl implements CreateRelationshipService{

    private final RelationshipRepository relationshipRepository;

    @Override
    public void create(User user, User friend) {
        relationshipRepository.save(Relationship.create(user, friend));
    }
}
