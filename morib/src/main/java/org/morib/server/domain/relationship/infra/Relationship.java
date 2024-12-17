package org.morib.server.domain.relationship.infra;

import jakarta.persistence.*;
import lombok.*;
import org.morib.server.domain.relationship.infra.type.RelationLevel;
import org.morib.server.domain.user.infra.User;
import org.morib.server.global.common.BaseTimeEntity;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Builder
public class Relationship extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "relationship_id")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "friend_id")
    private User friend;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RelationLevel relationLevel;

    public static Relationship create(User user, User friend) {
        return Relationship.builder()
                .user(user)
                .friend(friend)
                .relationLevel(RelationLevel.UNCONNECTED)
                .build();
    }

    public void updateRelationLevelToConnect() {
        this.relationLevel = RelationLevel.CONNECTED;
    }
}
