package org.morib.server.domain.allowedGroup.infra;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AllowedGroupRepository extends JpaRepository<AllowedGroup, Long> {
    List<AllowedGroup> findAllByUserId(Long userId);
    int countByUserId(Long userId);
}
