package org.morib.server.global.exception;

import org.morib.server.global.message.ErrorMessage;

public class SSEConnectionException extends BusinessException {
    public SSEConnectionException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
