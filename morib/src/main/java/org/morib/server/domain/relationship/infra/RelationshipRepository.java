package org.morib.server.domain.relationship.infra;

import org.morib.server.domain.relationship.infra.type.RelationLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;
import java.util.Optional;

public interface RelationshipRepository extends JpaRepository<Relationship, Long> {
    @Query("SELECT r " +
            "FROM Relationship r " +
            "WHERE (r.user.id = :userId OR r.friend.id = :userId) " +
            "AND r.relationLevel = :relationLevel")
    List<Relationship> findByUserIdOrFriendIdAndRelationLevel(
            @Param("userId") Long userId,
            @Param("relationLevel") RelationLevel relationLevel);

    @Query("SELECT r " +
            "FROM Relationship r " +
            "WHERE (r.user.id = :userId AND r.friend.id = :friendId) " +
            "AND r.relationLevel = :relationLevel")
    Optional<Relationship> findByUserIdAndFriendIdAndRelationLevel(
            @Param("userId") Long userId,
            @Param("friendId") Long friendId,
            @Param("relationLevel") RelationLevel relationLevel
    );

    @Query("SELECT r FROM Relationship r " +
            "WHERE (r.user.id = :userId AND r.friend.id = :friendId) " +
            "OR (r.user.id = :friendId AND r.friend.id = :userId) " +
            "AND r.relationLevel = :relationLevel")
    Optional<Relationship> findByUserAndFriendBothSide(
            @Param("userId") Long userId,
            @Param("friendId") Long friendId,
            @Param("relationLevel") RelationLevel relationLevel);

    @Query("SELECT r FROM Relationship r " +
            "WHERE (r.user.id = :userId AND r.friend.id = :friendId) " +
            "   OR (r.user.id = :friendId AND r.friend.id = :userId)")
    Optional<Relationship> findByUserAndFriend(
            @Param("userId") Long userId,
            @Param("friendId") Long friendId);


}
