package org.morib.server.domain.relationship.infra;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.morib.server.domain.relationship.infra.type.RelationLevel;
import org.morib.server.domain.user.infra.User;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Relationship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "permission_id")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User friend;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RelationLevel relationLevel;
}
