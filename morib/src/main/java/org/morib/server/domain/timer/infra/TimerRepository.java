package org.morib.server.domain.timer.infra;

import java.util.List;
import org.morib.server.domain.user.infra.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

public interface TimerRepository extends JpaRepository<Timer, Long> {
    List<Timer> findByUserAndTargetDate(User user, LocalDate targetDate);


    List<Timer> findByUser(User user);

}
