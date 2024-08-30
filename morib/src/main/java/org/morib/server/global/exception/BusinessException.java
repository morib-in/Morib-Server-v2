package org.morib.server.global.exception;

import lombok.Getter;
import org.morib.server.global.message.ErrorMessage;

@Getter
public class BusinessException extends RuntimeException {
    public ErrorMessage errorMessage;

    public BusinessException(ErrorMessage errorMessage) {
        super(errorMessage.getMessage());
        this.errorMessage = errorMessage;
    }
}
