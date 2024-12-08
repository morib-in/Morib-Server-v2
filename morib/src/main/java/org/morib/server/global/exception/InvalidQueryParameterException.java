package org.morib.server.global.exception;

import org.morib.server.global.message.ErrorMessage;

public class InvalidQueryParameterException extends BusinessException {
    public InvalidQueryParameterException(ErrorMessage errorMessage) {
        super(errorMessage);
    }

}
