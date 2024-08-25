package org.morib.server.domain.timer.infra;

import java.util.List;
import org.morib.server.domain.user.infra.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimerRepository extends JpaRepository<Timer, Long> {

    List<Timer> findByUser(User user);
}
