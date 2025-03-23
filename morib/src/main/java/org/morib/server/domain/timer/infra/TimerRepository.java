package org.morib.server.domain.timer.infra;

import org.morib.server.domain.user.infra.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface TimerRepository extends JpaRepository<Timer, Long> {
    List<Timer> findByUserAndTargetDate(User user, LocalDate targetDate);
    List<Timer> findByUser(User user);
    @Query("SELECT t.task.id FROM Timer t WHERE t.task.id IN :taskIds AND t.targetDate = :targetDate")
    Set<Long> findExistingTaskIdsByTargetDate(List<Long> taskIds, LocalDate targetDate);
    Timer findByTaskIdAndTargetDate(Long taskId, LocalDate targetDate);
}
