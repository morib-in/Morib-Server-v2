package org.morib.server.domain.user.infra;

import org.morib.server.domain.user.infra.type.Platform;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByRefreshToken(String refreshToken);
    Optional<User> findByPlatformAndSocialId(Platform platform, String socialId);

}
