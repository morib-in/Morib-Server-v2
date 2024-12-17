package org.morib.server.domain.relationship.application;

import org.morib.server.domain.relationship.infra.Relationship;

public interface DeleteRelationshipService {
    void delete(Relationship relationship);
}
