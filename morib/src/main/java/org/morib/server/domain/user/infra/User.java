package org.morib.server.domain.user.infra;

import jakarta.persistence.*;
import lombok.*;
import org.morib.server.domain.category.infra.Category;
import org.morib.server.domain.user.infra.type.Platform;
import org.morib.server.domain.user.infra.type.Role;
import org.morib.server.global.common.BaseTimeEntity;
import org.morib.server.global.oauth2.userinfo.OAuth2UserInfo;

import java.util.Set;

@Entity
@Getter
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String name;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Platform platform;
    private String imageUrl;
    private String refreshToken;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Category> categories;
    @Enumerated(EnumType.STRING)
    private Role role;
    private String socialId; // 로그인한 소셜 타입의 식별자 값 (일반 로그인인 경우 null)

    // 유저 권한 설정 메소드
    public void authorizeUser() {
        this.role = Role.USER;
    }

    public void updateRefreshToken(String updateRefreshToken) {
        this.refreshToken = updateRefreshToken;
    }

    public static User createByOAuth2UserInfo(Platform platform, OAuth2UserInfo oauth2UserInfo) {
        return User.builder()
                .platform(platform)
                .socialId(oauth2UserInfo.getId())
                .email(oauth2UserInfo.getEmail())
                .name(oauth2UserInfo.getName())
                .imageUrl(oauth2UserInfo.getImageUrl())
                .role(Role.GUEST)
                .build();
    }
}
