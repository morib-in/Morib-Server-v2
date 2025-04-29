package org.morib.server.domain.recommendSite.infra;

import jakarta.persistence.*;
import lombok.*;
import org.morib.server.domain.user.infra.type.InterestArea;
import org.morib.server.global.common.BaseTimeEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "recommend_sites")
public class RecommendSite extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recommend_site_id")
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InterestArea interestArea;
    @Column(nullable = false)
    private String favicon;
    @Column(nullable = false)
    private String siteName;
    @Column(nullable = false)
    private String pageName;
    @Column(nullable = false)
    private String siteUrl;
}