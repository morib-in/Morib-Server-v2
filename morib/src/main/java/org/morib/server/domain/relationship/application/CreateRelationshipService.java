package org.morib.server.domain.relationship.application;

import org.morib.server.domain.user.infra.User;

public interface CreateRelationshipService {
    void create(User user, User friend);
}
