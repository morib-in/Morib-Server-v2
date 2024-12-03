package org.morib.server.domain.relationship.application;

import lombok.RequiredArgsConstructor;
import org.morib.server.domain.relationship.infra.Relationship;
import org.morib.server.domain.relationship.infra.RelationshipRepository;
import org.morib.server.domain.relationship.infra.type.RelationLevel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FetchFriendsServiceImpl implements FetchFriendsService{

    private final RelationshipRepository relationshipRepository;

    @Override
    public List<Relationship> fetch(Long userId) {
        return relationshipRepository.findByUserIdOrFriendIdAndRelationLevel(userId, RelationLevel.CONNECTED);
    }
}
