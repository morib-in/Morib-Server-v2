package org.morib.server.domain.user.infra;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WaitingUserWindowRepository extends JpaRepository<WaitingUserWindow, Long> {
}
