package org.morib.server.global.message;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum ErrorMessage {
    /**
     * 400 Bad Request
     */
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    TYPE_MISMATCH(HttpStatus.BAD_REQUEST, "잘못된 형식입니다"),
    INVALID_URL(HttpStatus.BAD_REQUEST, "요청된 url이 유효하지 않습니다."),
    INVALID_EMAIL(HttpStatus.BAD_REQUEST, "유효하지 않은 이메일 형식입니다."),
    INVALID_TASK_IN_TODO(HttpStatus.BAD_REQUEST, "완료된 태스크는 할 일에 추가하거나 타이머를 실행할 수 없습니다."),
    CANNOT_ADD_YOURSELF(HttpStatus.BAD_REQUEST, "자기 자신을 친구로 추가할 수 없습니다."),
    WITHOUT_TIMER_STATUS(HttpStatus.BAD_REQUEST, "timerStatus 값이 없습니다. Request Header에 추가했는지 확인해주세요."),
    /**
     * 401 Unauthorized
     */
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증되지 않은 사용자입니다."),
    JWT_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "사용자의 로그인 검증을 실패했습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효한 토큰이 아닙니다."),
    FAILED_WITHDRAW(HttpStatus.UNAUTHORIZED, "회원 탈퇴에 실패했습니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다. 재발급해주세요."),
    SSE_CONNECT_FAILED(HttpStatus.BAD_REQUEST, "SSE 연결에 실패했습니다."),
    /**
     * 403 Forbidden
     */
    FORBIDDEN(HttpStatus.FORBIDDEN, "리소스 접근 권한이 없습니다."),

    /**
     * 404 Not Found
     */
    NOT_FOUND(HttpStatus.NOT_FOUND, "대상을 찾을 수 없습니다."),

    /**
     * 405 Method Not Allowed
     */
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "잘못된 HTTP Method 요청입니다."),
    /**
     * 409 Conflict
     */
    ALREADY_FRIEND(HttpStatus.CONFLICT, "이미 친구가 된 사용자입니다."),
    ALREADY_FRIEND_REQUEST(HttpStatus.CONFLICT, "이미 요청된 사용자입니다."),
    DUPLICATE_RESOURCE(HttpStatus.CONFLICT, "이미 존재하는 리소스입니다."),
    /**
     * 500 Internal Server Error
     */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류입니다."),

    ;

    private final HttpStatus httpStatus;
    private final String message;

}
