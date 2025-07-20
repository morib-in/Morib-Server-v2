package org.morib.server.global.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
@Slf4j
public class CustomAuthorizationRequestResolver implements OAuth2AuthorizationRequestResolver {

    public static final String CLIENT_TYPE_PARAM = "client_type";
    public static final String STATE_CLIENT_TYPE_KEY = "clientType";
    public static final String STATE_CSRF_KEY = "csrf";

    private final OAuth2AuthorizationRequestResolver defaultAuthorizationRequestResolver;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public CustomAuthorizationRequestResolver(
            ClientRegistrationRepository clientRegistrationRepository) {

        this.defaultAuthorizationRequestResolver =
                new DefaultOAuth2AuthorizationRequestResolver(
                        clientRegistrationRepository, "/oauth2/authorization");
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
        OAuth2AuthorizationRequest authorizationRequest =
                this.defaultAuthorizationRequestResolver.resolve(request);

        return authorizationRequest != null ?
                customAuthorizationRequest(authorizationRequest, request) :
                null;
    }

    @Override
    public OAuth2AuthorizationRequest resolve(
            HttpServletRequest request, String clientRegistrationId) {

        OAuth2AuthorizationRequest authorizationRequest =
                this.defaultAuthorizationRequestResolver.resolve(
                        request, clientRegistrationId);

        return authorizationRequest != null ?
                customAuthorizationRequest(authorizationRequest, request) :
                null;
    }

    private OAuth2AuthorizationRequest customAuthorizationRequest(
            OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request) {


        if(authorizationRequest.getAttribute("registration_id").equals("apple")) {
            log.info("now in here before return apple social login redirection");

            return OAuth2AuthorizationRequest.from(authorizationRequest)
                .redirectUri(authorizationRequest.getRedirectUri())
                .build();
        }

        String clientType = request.getParameter(CLIENT_TYPE_PARAM);
        // clientType 파라미터가 없으면 기본값 "web" 사용
        if (!StringUtils.hasText(clientType)) {
            clientType = "web";
        }

        String originalState = authorizationRequest.getState();

        Map<String, String> newStateMap = new HashMap<>();
        newStateMap.put(STATE_CSRF_KEY, originalState);
        newStateMap.put(STATE_CLIENT_TYPE_KEY, clientType);

        try {
            String newStateJson = objectMapper.writeValueAsString(newStateMap);
            String encodedNewState = Base64.getUrlEncoder().withoutPadding().encodeToString(newStateJson.getBytes(StandardCharsets.UTF_8));

            log.debug("Original State (CSRF): {}", originalState);
            log.debug("Client Type: {}", clientType);
            log.debug("Encoded New State: {}", encodedNewState);

            Map<String, Object> additionalParameters =
                    new LinkedHashMap<>(authorizationRequest.getAdditionalParameters());
            additionalParameters.put("access_type", "offline");
            additionalParameters.put("prompt", "consent");

            return OAuth2AuthorizationRequest.from(authorizationRequest)
                    .state(encodedNewState)
                    .redirectUri("https://api.morib.in/login/oauth2/code/google")
                    .additionalParameters(additionalParameters)
                    .build();

        } catch (JsonProcessingException e) {
            log.error("Error encoding state to JSON", e);
            return authorizationRequest;
        }
    }
}
