package org.morib.server.domain.category.infra;

import java.util.Optional;
import org.morib.server.domain.user.infra.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("select e from Category e where " +
            "e.user.id = :userId " +
            "and e.startDate between :startDate and :endDate " +
            "or e.endDate between :startDate and :endDate " +
            "or (e.startDate <= :startDate and e.endDate >= :endDate)")
    List<Category> findByUserIdInRange(Long userId, LocalDate startDate, LocalDate endDate);


    Optional<Category> findByIdAndUser(Long categoryId, User user);

    Optional<Category> findByUserAndId(User findUser, Long categoryId);

}
