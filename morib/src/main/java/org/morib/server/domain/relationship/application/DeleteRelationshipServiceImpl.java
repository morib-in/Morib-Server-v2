package org.morib.server.domain.relationship.application;

import lombok.RequiredArgsConstructor;
import org.morib.server.domain.relationship.infra.Relationship;
import org.morib.server.domain.relationship.infra.RelationshipRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteRelationshipServiceImpl implements DeleteRelationshipService{

    private final RelationshipRepository relationshipRepository;

    @Override
    public void delete(Relationship relationship) {
        relationshipRepository.delete(relationship);
    }
}
