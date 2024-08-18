package org.morib.server.domain.user.infra;

import org.morib.server.domain.mset.infra.Mset;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
