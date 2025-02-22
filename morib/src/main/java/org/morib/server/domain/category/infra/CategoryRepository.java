package org.morib.server.domain.category.infra;

import java.util.Optional;
import org.morib.server.domain.user.infra.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByUserAndId(User findUser, Long categoryId);

    @Query("SELECT DISTINCT c FROM Category c " +
            "LEFT JOIN FETCH c.tasks t " +
            "LEFT JOIN FETCH t.timers ti " +
            "WHERE c.user.id = :userId " +
            "AND (t IS NULL OR " +
            "(t.startDate BETWEEN :startDate AND :endDate " +
            "OR (t.startDate <= :endDate AND t.endDate IS NULL) " +
            "OR (t.startDate <= :endDate AND t.endDate >= :startDate))) " +
            "ORDER BY c.createdAt ASC ")
    List<Category> findByUserIdWithFilteredTasksAndTimers(Long userId, LocalDate startDate, LocalDate endDate);

}
