package org.morib.server.domain.relationship;

import org.morib.server.annotation.Manager;
import org.morib.server.domain.relationship.infra.Relationship;

@Manager
public class RelationshipManager {

    public void updateRelationLevelToConnect(Relationship relationship) {
        relationship.updateRelationLevelToConnect();
    }
}
