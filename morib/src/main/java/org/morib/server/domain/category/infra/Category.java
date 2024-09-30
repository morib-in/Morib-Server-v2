package org.morib.server.domain.category.infra;

import jakarta.persistence.*;
import lombok.*;
import org.morib.server.domain.task.infra.Task;
import org.morib.server.domain.user.infra.User;
import org.morib.server.global.common.BaseTimeEntity;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Builder
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Category extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;
    @Column(nullable = false)
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Task> tasks = new LinkedHashSet<>();

    public static Category create(String name, User user) {
        return Category.builder()
                .name(name)
                .user(user)
                .build();
    }

}
