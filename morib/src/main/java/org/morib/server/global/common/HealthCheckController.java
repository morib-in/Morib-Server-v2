package org.morib.server.global.common;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HealthCheckController {

    private final Environment environment;

    @GetMapping("/profile")
    public String getProfile() {
        String[] profiles = environment.getActiveProfiles();
        if (profiles.length == 0) {
            return "UP";
        }
        return profiles[0];
    }
}
