package org.morib.server.domain.relationship.application;

import lombok.RequiredArgsConstructor;
import org.morib.server.domain.relationship.infra.Relationship;
import org.morib.server.domain.relationship.infra.RelationshipRepository;
import org.morib.server.domain.relationship.infra.type.RelationLevel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FetchRelationshipServiceImpl implements FetchRelationshipService {

    private final RelationshipRepository relationshipRepository;

    @Override
    public List<Relationship> fetchConnectedRelationship(Long userId) {
        return relationshipRepository.findByUserIdOrFriendIdAndRelationLevel(userId, RelationLevel.CONNECTED);
    }

    @Override
    public List<Relationship> fetchUnconnectedRelationship(Long userId) {
        return relationshipRepository.findByUserIdOrFriendIdAndRelationLevel(userId, RelationLevel.UNCONNECTED);
    }
}
