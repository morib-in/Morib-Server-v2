package org.morib.server.domain.permission.infra;

import org.morib.server.domain.mset.infra.Mset;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, Long> {

}
