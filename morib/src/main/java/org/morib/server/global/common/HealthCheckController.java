package org.morib.server.global.common;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HealthCheckController {
    @GetMapping("/profile")
    public String getProfile() {
        return "UP";
    }
}
