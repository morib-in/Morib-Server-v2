package org.morib.server.global.common;

import org.morib.server.global.message.ErrorMessage;
import org.morib.server.global.message.SuccessMessage;
import org.springframework.http.ResponseEntity;

public interface ApiResponseUtil {
    static ResponseEntity<BaseResponse<?>> success(SuccessMessage successMessage) {
        return ResponseEntity.status(successMessage.getHttpStatus())
                .body(BaseResponse.of(successMessage));
    }

    static <T> ResponseEntity<BaseResponse<?>> success(SuccessMessage successMessage, T data) {
        return ResponseEntity.status(successMessage.getHttpStatus())
                .body(BaseResponse.of(successMessage, data));
    }

    static ResponseEntity<BaseResponse<?>> failure(ErrorMessage errorMessage) {
        return ResponseEntity.status(errorMessage.getHttpStatus())
                .body(BaseResponse.of(errorMessage));
    }

}
