package org.morib.server.global.exception;

import org.morib.server.global.message.ErrorMessage;

public class InvalidStateException extends BusinessException {
    public InvalidStateException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
