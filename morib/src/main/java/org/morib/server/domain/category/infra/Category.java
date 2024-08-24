package org.morib.server.domain.category.infra;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.morib.server.domain.task.infra.Task;
import org.morib.server.domain.user.infra.User;
import org.morib.server.global.common.BaseTimeEntity;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private Long userId;
    @Column(nullable = false)
    private LocalDate startDate;
    private LocalDate endDate;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Task> tasks;
}
