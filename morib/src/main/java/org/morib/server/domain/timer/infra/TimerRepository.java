package org.morib.server.domain.timer.infra;

import org.morib.server.domain.user.infra.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface TimerRepository extends JpaRepository<Timer, Long> {
    Optional<Timer> findByUserAndTargetDate(User user, LocalDate targetDate);
}
