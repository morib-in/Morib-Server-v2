package org.morib.server.global.common.util;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.morib.server.annotation.IsToday;

import java.time.LocalDate;
import java.util.Objects;

public class IsTodayValidator implements ConstraintValidator<IsToday, LocalDate> {

    @Override
    public void initialize(IsToday constraintAnnotation) {
        // 어노테이션 초기화 (필요한 경우 로직 추가)
    }

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return Objects.equals(value, LocalDate.now());
    }
}