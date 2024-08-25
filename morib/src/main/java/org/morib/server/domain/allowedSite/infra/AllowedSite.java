package org.morib.server.domain.allowedSite.infra;

import jakarta.persistence.*;
import lombok.*;
import org.morib.server.domain.allowedSite.infra.type.OwnerType;
import org.morib.server.global.common.BaseTimeEntity;

@Builder
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AllowedSite extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "allowed_site_id")
    private Long id;
    @Column(nullable = false)
    private String siteName;
    @Column(nullable = false)
    private String siteUrl;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OwnerType ownerType;
    @Column(nullable = false)
    private Long ownerId;

    public static AllowedSite create(String siteName, String siteUrl, OwnerType ownerType, Long ownerId) {
        return AllowedSite.builder()
                .siteName(siteName)
                .siteUrl(siteUrl)
                .ownerType(ownerType)
                .ownerId(ownerId)
                .build();
    }
}
