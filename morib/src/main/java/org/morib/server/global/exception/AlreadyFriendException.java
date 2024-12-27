package org.morib.server.global.exception;

import org.morib.server.global.message.ErrorMessage;

public class AlreadyFriendException extends BusinessException{
    public AlreadyFriendException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
