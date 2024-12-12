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
import org.morib.server.domain.allowedSite.infra.AllowedSite;
import org.morib.server.domain.user.infra.User;

@Builder
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AllowedGroup {

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
    private Set<AllowedSite> allowedSites = new LinkedHashSet<>();


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
