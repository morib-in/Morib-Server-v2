package org.morib.server.domain.allowedGroup.infra;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AllowedGroupRepository extends JpaRepository<AllowedGroup, Long> {

    @Query("select ag from AllowedGroup  ag join fetch ag.allowedSites where ag.user.id = :userId")
    List<AllowedGroup> findAllFetchJoinByUserId(Long userId);

    List<AllowedGroup> findAllByUserId(Long userId);
}
