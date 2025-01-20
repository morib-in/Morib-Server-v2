package org.morib.server.global.common;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.morib.server.annotation.ValidUrl;

import java.net.HttpURLConnection;
import java.net.URL;

public class UrlValidator implements ConstraintValidator<ValidUrl, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true; // null이나 빈 값은 유효성 검증을 통과 (필수 여부는 @NotBlank 등으로 처리)
        }
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(value).openConnection();
            connection.setRequestMethod("HEAD");
            connection.setConnectTimeout(3000);
            connection.connect();
            int responseCode = connection.getResponseCode();
            return responseCode == HttpURLConnection.HTTP_OK;
        } catch (Exception e) {
            return false;
        }
    }
}


