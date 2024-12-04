package org.morib.server.domain.relationship.infra;

import org.morib.server.domain.relationship.infra.type.RelationLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RelationshipRepository extends JpaRepository<Relationship, Long> {
    @Query("SELECT r " +
            "FROM Relationship r " +
            "WHERE (r.user.id = :userId OR r.friend.id = :userId) " +
            "AND r.relationLevel = :relationLevel")
    List<Relationship> findByUserIdOrFriendIdAndRelationLevel(
            @Param("userId") Long userId,
            @Param("relationLevel") RelationLevel relationLevel);
}
