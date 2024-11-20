package org.morib.server.domain.allowedSite.infra;

import jakarta.persistence.*;
import lombok.*;
import org.morib.server.domain.allowedGroup.infra.AllowedGroup;
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

    @ManyToOne
    @JoinColumn(name = "allowed_group_id")
    private AllowedGroup allowedGroup;


    public static AllowedSite create(String siteName, String siteUrl) {
        return AllowedSite.builder()
                .siteName(siteName)
                .siteUrl(siteUrl)
                .build();
    }
}
