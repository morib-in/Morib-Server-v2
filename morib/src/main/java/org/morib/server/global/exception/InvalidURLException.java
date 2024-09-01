package org.morib.server.global.exception;

import org.morib.server.global.message.ErrorMessage;

public class InvalidURLException extends BusinessException{
    public InvalidURLException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
