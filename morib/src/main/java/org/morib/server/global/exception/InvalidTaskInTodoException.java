package org.morib.server.global.exception;

import org.morib.server.global.message.ErrorMessage;

public class InvalidTaskInTodoException extends BusinessException{
    public InvalidTaskInTodoException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
