package org.morib.server.global.exception;

import org.morib.server.global.message.ErrorMessage;

public class UnauthorizedException extends BusinessException {
    public UnauthorizedException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
