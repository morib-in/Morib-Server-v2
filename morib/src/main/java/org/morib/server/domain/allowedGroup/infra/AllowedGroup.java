package org.morib.server.domain.allowedGroup.infra;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;
import org.morib.server.domain.allowedGroup.infra.type.ColorCode;
import org.morib.server.domain.allowedSite.infra.AllowedSite;
import org.morib.server.domain.user.infra.User;
import org.morib.server.global.common.BaseTimeEntity;

@Builder
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AllowedGroup extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String colorCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "allowedGroup", fetch = FetchType.LAZY,
        cascade = CascadeType.ALL, orphanRemoval = true)
    @BatchSize(size = 20)
    private Set<AllowedSite> allowedSites = new LinkedHashSet<>();

    public static AllowedGroup create(User user, int nameIndex) {
        return AllowedGroup.builder()
                .name("내 허용 서비스 세트 " + nameIndex)
                .colorCode(ColorCode.ALLOWED_SERVICE_COLOR_CODE_GREY.getHexCode())
                .user(user)
                .build();
    }

    public static AllowedGroup createWithBody(User user, String name, String colorCode) {
        return AllowedGroup.builder()
                .name(name)
                .colorCode(colorCode)
                .user(user)
                .build();
    }

    public void updateAll(String colorCode, String name) {
        this.colorCode = colorCode;
        this.name = name;
    }

    public void updateColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public void updateName(String name) {
        this.name = name;
    }
}
