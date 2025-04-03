package org.morib.server.domain.user.infra;

import jakarta.persistence.*;
import lombok.*;
import org.morib.server.domain.allowedGroup.infra.AllowedGroup;
import org.morib.server.domain.category.infra.Category;
import org.morib.server.domain.recentAllowedGroup.infra.RecentAllowedGroup;
import org.morib.server.domain.user.infra.type.InterestArea;
import org.morib.server.domain.user.infra.type.Platform;
import org.morib.server.domain.user.infra.type.Role;
import org.morib.server.global.common.BaseTimeEntity;
import org.morib.server.global.oauth2.userinfo.OAuth2UserInfo;

import java.util.LinkedHashSet;
import java.util.Set;

import static org.morib.server.global.common.Constants.INVALID_REFRESH_TOKEN;

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
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AllowedGroup> allowedGroups = new LinkedHashSet<>();
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RecentAllowedGroup> recentAllowedGroups = new LinkedHashSet<>();
    @Enumerated(EnumType.STRING)
    private Role role;

    private InterestArea interestArea;

    private String socialId; // 로그인한 소셜 타입의 식별자 값 (일반 로그인인 경우 null)
    private String social_refreshToken;
    private boolean isPushEnabled;
    private boolean isOnboardingCompleted;
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
                .isPushEnabled(false)
                .social_refreshToken(null)
                .build();
    }

    public void updateProfile(String name, String imageUrl, boolean isPushEnabled) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.isPushEnabled = isPushEnabled;
    }

    public void updateSocialRefreshToken(String socialRefreshToken) {
        this.social_refreshToken = socialRefreshToken;
    }

    public void invalidateRefreshToken() {
        this.refreshToken = INVALID_REFRESH_TOKEN;
    }

    public void updateUserInterestArea(InterestArea interestArea) {
        this.interestArea = interestArea;
    }

    public void completeOnboarding() {
        this.isOnboardingCompleted = true;
    }
}
