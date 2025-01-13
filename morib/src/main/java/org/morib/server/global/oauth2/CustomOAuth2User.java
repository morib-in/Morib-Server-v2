package org.morib.server.global.oauth2;

import lombok.Getter;
import org.morib.server.domain.user.infra.type.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collection;
import java.util.Map;

@Getter
public class CustomOAuth2User extends DefaultOAuth2User {

    private Role role;
    private Long userId;
    private String registrationId;
    private String principalName;

    public CustomOAuth2User(Collection<? extends GrantedAuthority> authorities,
                            Map<String, Object> attributes, String nameAttributeKey,
                            Role role, Long userId, String registrationId, String principalName) {
        super(authorities, attributes, nameAttributeKey);
        this.role = role;
        this.userId = userId;
        this.registrationId = registrationId;
        this.principalName = principalName;
    }
}