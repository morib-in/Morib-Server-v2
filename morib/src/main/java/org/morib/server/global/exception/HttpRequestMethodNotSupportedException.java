package org.morib.server.global.exception;

import org.morib.server.global.message.ErrorMessage;

public class HttpRequestMethodNotSupportedException extends BusinessException {
    public HttpRequestMethodNotSupportedException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
