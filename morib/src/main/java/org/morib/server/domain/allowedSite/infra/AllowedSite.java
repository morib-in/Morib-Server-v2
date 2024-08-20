package org.morib.server.domain.allowedSite.infra;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.morib.server.domain.allowedSite.infra.type.OwnerType;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AllowedSite {
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
}
