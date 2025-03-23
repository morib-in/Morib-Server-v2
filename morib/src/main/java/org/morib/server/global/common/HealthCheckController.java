package org.morib.server.global.common;

import lombok.RequiredArgsConstructor;
import org.morib.server.global.message.SuccessMessage;
import org.morib.server.global.userauth.CustomUserDetails;
import org.morib.server.global.userauth.PrincipalHandler;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequiredArgsConstructor
public class HealthCheckController {

    private final Environment environment;
    private final PrincipalHandler principalHandler;
    public static final ConcurrentHashMap<Long, LocalDateTime> userLastAccessTimeMap = new ConcurrentHashMap<>();

    @GetMapping("/health")
    public String healthCheck() {
        return "UP";
    }

    @GetMapping("/profile")
    public String getActiveProfile() {
        String[] profiles = environment.getActiveProfiles();
        if (profiles.length == 0) {
            return "UP";
        }
        return profiles[0];
    }

    @GetMapping("/api/v2/heart-beat")
    public ResponseEntity<BaseResponse<?>> heartBeat(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Long userId = principalHandler.getUserIdFromUserDetails(customUserDetails);
        userLastAccessTimeMap.put(userId, LocalDateTime.now());
        return ApiResponseUtil.success(SuccessMessage.SUCCESS);
    }

    public boolean isUserActive(Long userId) {
        if (userLastAccessTimeMap.containsKey(userId)) {
            LocalDateTime lastAccessTime = userLastAccessTimeMap.get(userId);
            return lastAccessTime.isAfter(LocalDateTime.now().minusMinutes(2));
        }
        return false;
    }
}
