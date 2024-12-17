package org.morib.server.domain.relationship.application;

import org.morib.server.domain.user.infra.User;

public interface ValidateRelationshipService {
    void validateRelationshipByUserAndFriend(User user, User friend);
}
