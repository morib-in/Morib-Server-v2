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

    ;

    private final HttpStatus httpStatus;
    private final String message;

}
