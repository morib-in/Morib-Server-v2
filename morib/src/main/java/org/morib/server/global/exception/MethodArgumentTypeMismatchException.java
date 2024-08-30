package org.morib.server.global.exception;

import org.morib.server.global.message.ErrorMessage;

public class MethodArgumentTypeMismatchException extends BusinessException {
    public MethodArgumentTypeMismatchException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
