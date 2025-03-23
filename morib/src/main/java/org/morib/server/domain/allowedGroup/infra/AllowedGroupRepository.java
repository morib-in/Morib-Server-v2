package org.morib.server.domain.allowedGroup.infra;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AllowedGroupRepository extends JpaRepository<AllowedGroup, Long> {
    List<AllowedGroup> findAllByUserId(Long userId);
    int countByUserId(Long userId);
}
