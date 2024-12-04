package org.morib.server.domain.relationship.application;

import org.morib.server.domain.relationship.infra.Relationship;

import java.util.List;

public interface FetchRelationshipService {
    List<Relationship> fetchConnectedRelationship(Long userId);
}
