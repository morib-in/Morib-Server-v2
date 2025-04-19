package org.morib.server.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.morib.server.global.common.util.IsTodayValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER}) // 필드와 파라미터에 적용 가능하도록 설정
@Retention(RetentionPolicy.RUNTIME) // 런타임 시에 어노테이션 정보 유지
@Constraint(validatedBy = IsTodayValidator.class) // 이 어노테이션의 유효성 검사를 수행할 Validator 클래스 지정
public @interface IsToday {
    String message() default "날짜는 오늘이어야 합니다."; // 유효성 검사 실패 시 기본 메시지

    Class<?>[] groups() default {}; // 유효성 검사 그룹 지정 (기본값: 없음)

    Class<? extends Payload>[] payload() default {}; // 유효성 검사 페이로드 지정 (기본값: 없음)
}
