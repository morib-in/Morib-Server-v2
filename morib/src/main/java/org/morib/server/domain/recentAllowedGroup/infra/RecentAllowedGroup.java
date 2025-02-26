package org.morib.server.domain.recentAllowedGroup.infra;

import jakarta.persistence.*;
import lombok.*;
import org.morib.server.domain.allowedGroup.infra.AllowedGroup;
import org.morib.server.domain.user.infra.User;
import org.morib.server.global.common.BaseTimeEntity;

@Builder
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RecentAllowedGroup extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @OneToOne
    @JoinColumn(name = "allowed_group_id")
    private AllowedGroup selectedAllowedGroup;

    public static RecentAllowedGroup create(User user, AllowedGroup allowedGroup) {
        return RecentAllowedGroup.builder()
                .user(user)
                .selectedAllowedGroup(allowedGroup)
                .build();
    }
}
