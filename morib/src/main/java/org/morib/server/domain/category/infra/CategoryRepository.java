package org.morib.server.domain.category.infra;

import java.util.Optional;
import org.morib.server.domain.user.infra.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByUserAndId(User findUser, Long categoryId);

    @Query("SELECT c FROM Category c " +
            "LEFT JOIN FETCH c.tasks t " +
            "LEFT JOIN FETCH t.timers ti " +
            "WHERE c.user.id = :userId " +
            "ORDER BY c.createdAt ASC ")
    List<Category> findByUserIdAndFetchTasksAndTimers(Long userId);

    @Query("SELECT c FROM Category c " +
            "LEFT JOIN FETCH c.tasks t " +
            "LEFT JOIN FETCH t.timers ti " +
            "WHERE c.user.id = :userId " +
            "AND t.id = :taskId")
    Category findByUserIdAndTaskId(Long userId, Long taskId);

}
