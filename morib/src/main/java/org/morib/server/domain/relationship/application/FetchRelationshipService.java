package org.morib.server.domain.relationship.application;

import org.morib.server.domain.relationship.infra.Relationship;
import org.morib.server.domain.relationship.infra.type.RelationLevel;
import org.morib.server.domain.user.infra.User;

import java.util.List;

public interface FetchRelationshipService {
    List<Relationship> fetchConnectedRelationship(Long userId);
    List<Relationship> fetchUnconnectedRelationship(Long userId);
    Relationship fetchRelationshipByUserIdAndFriendId(Long userId, Long friendId, RelationLevel relationLevel);
    void validateRelationshipByUserAndFriend(User user, User friend);

}
