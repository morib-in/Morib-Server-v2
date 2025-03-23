package org.morib.server.global.common;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.morib.server.annotation.ValidUrl;

public class UrlValidator implements ConstraintValidator<ValidUrl, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
//        if (value == null || value.isBlank()) {
//            return true; // null이나 빈 값은 유효성 검증을 통과 (필수 여부는 @NotBlank 등으로 처리)
//        }
//        try {
//            HttpURLConnection connection = (HttpURLConnection) new URL(value).openConnection();
//            connection.setRequestMethod("GET"); // ⭐ GET으로 변경
//            connection.setConnectTimeout(3000);
//            connection.setReadTimeout(3000);
//
//            // ⭐ 리다이렉트 허용 추가
//            connection.setInstanceFollowRedirects(true);
//            connection.setRequestProperty("User-Agent",
//                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
//                            "AppleWebKit/537.36 (KHTML, like Gecko) " +
//                            "Chrome/120.0.0.0 Safari/537.36");
//
//            connection.connect();
//            int responseCode = connection.getResponseCode();
//            System.out.println("URL Validation Response Code : " + responseCode);
//
//            // 200번대 응답 (200~299) 허용
//            return 200 <= responseCode && responseCode < 500;
//        } catch (Exception e) {
//            System.out.println("URL Validation Error : "  + e.getMessage());
//            return false;
//        }
        return true;
    }
}


