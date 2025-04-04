package org.morib.server.global.common.util;

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

    public static String extractDomainFromRawUrl(String urlString) {
        if (urlString.contains("www.")) return urlString.split("www.")[1].split("/")[0];
        else if (urlString.contains("://")) return urlString.split("://")[1].split("/")[0];
        else return urlString;
    }
}

