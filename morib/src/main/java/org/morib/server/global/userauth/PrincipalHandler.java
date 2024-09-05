package org.morib.server.global.userauth;

import org.morib.server.global.exception.UnauthorizedException;
import org.morib.server.global.message.ErrorMessage;
import org.springframework.stereotype.Component;

@Component
public class PrincipalHandler {

    public Long getUserIdFromUserDetails(CustomUserDetails customUserDetails) {
        isUserDetailsNull(customUserDetails);
        return customUserDetails.getUserId();
    }

    public void isUserDetailsNull(
            final CustomUserDetails customUserDetails
    ) {
        if (customUserDetails == null) {
            throw new UnauthorizedException(ErrorMessage.JWT_UNAUTHORIZED);
        }
    }
}
