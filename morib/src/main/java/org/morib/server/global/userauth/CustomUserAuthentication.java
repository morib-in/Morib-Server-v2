package org.morib.server.global.userauth;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class CustomUserAuthentication extends UsernamePasswordAuthenticationToken {

    public CustomUserAuthentication(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }

    public static CustomUserAuthentication of(CustomUserDetails customUserDetails, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        return new CustomUserAuthentication(customUserDetails, credentials, authorities);
    }
}
