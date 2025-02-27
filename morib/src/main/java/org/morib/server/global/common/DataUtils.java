package org.morib.server.global.common;

import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Slf4j
@RequiredArgsConstructor
@Component
public class DataUtils {
    public static boolean isInRange(LocalDate idxDate, LocalDate startDate, LocalDate endDate) {
        return !idxDate.isBefore(startDate) && !idxDate.isAfter(endDate);
    }
    public Cookie getCookieForToken(String sub, String token) {
        log.info("getCookieForToken 진입.");
        Cookie cookie = new Cookie(sub, token);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setDomain("morib.in"); // 쿠키를 사용할 도메인
        cookie.setAttribute("SameSite", "None");
        log.info("getCookieForToken 완료.");
        log.info("Token Cookie: name={}, value={}, domain={}, path={}",
                cookie.getName(),
                cookie.getValue(),
                cookie.getDomain(),
                cookie.getPath());
        return cookie;
    }
}

