package org.morib.server.global.exception;

import org.morib.server.global.message.ErrorMessage;

public class AlreadyFriendRequestException extends BusinessException {
    public AlreadyFriendRequestException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
