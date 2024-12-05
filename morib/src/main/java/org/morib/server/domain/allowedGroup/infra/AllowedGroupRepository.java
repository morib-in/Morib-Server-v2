package org.morib.server.domain.allowedGroup.infra;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AllowedGroupRepository extends JpaRepository<AllowedGroup, Long> {
    void deleteById(Long groupId);

}
