package org.morib.server.global.exception;

import org.morib.server.global.message.ErrorMessage;

public class DuplicateResourceException extends BusinessException{
    public DuplicateResourceException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
