package org.morib.server.global.oauth2;

import lombok.Builder;
import lombok.Getter;
import org.morib.server.domain.user.infra.type.Platform;
import org.morib.server.global.oauth2.userinfo.AppleOAuth2UserInfo;
import org.morib.server.global.oauth2.userinfo.GoogleOAuth2UserInfo;
import org.morib.server.global.oauth2.userinfo.OAuth2UserInfo;

import java.util.Map;

@Getter
public class OAuthAttributes {

    private String nameAttributeKey;
    private OAuth2UserInfo oauth2UserInfo;

    @Builder
    public OAuthAttributes(String nameAttributeKey, OAuth2UserInfo oauth2UserInfo) {
        this.nameAttributeKey = nameAttributeKey;
        this.oauth2UserInfo = oauth2UserInfo;
    }

    public static OAuthAttributes of(Platform platform,
                                     String userNameAttributeName, Map<String, Object> attributes) {
        return ofGoogle(userNameAttributeName, attributes);
    }

    public static OAuthAttributes ofApple(Platform platform, Map<String, Object> attributes){
        return OAuthAttributes.builder()
            .nameAttributeKey("sub")
            .oauth2UserInfo(new AppleOAuth2UserInfo(attributes))
            .build();
    }


    public static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oauth2UserInfo(new GoogleOAuth2UserInfo(attributes))
                .build();
    }

}
