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

        String originalState = authorizationRequest.getState();



        String clientType = request.getParameter(CLIENT_TYPE_PARAM);
        // clientType 파라미터가 없으면 기본값 "web" 사용
        if (!StringUtils.hasText(clientType)) {
            clientType = "web";
        }


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

            if(authorizationRequest.getAttribute("registration_id").equals("apple")) {
                log.info("now in here before return apple social login redirection");
                log.info("now redirect url was this : {}", authorizationRequest.getRedirectUri());

                log.info("original state: {}", originalState);
                log.info("encoded new state: {}", encodedNewState);
                log.info("Apple용 encodedNewState 사용 (기존 시스템과 일관성 유지)");
                
                // Apple의 경우 추가 파라미터를 Apple 스펙에 맞게 조정
                Map<String, Object> appleAdditionalParameters = new LinkedHashMap<>(authorizationRequest.getAdditionalParameters());
                appleAdditionalParameters.put("response_mode", "form_post");
                
                return OAuth2AuthorizationRequest.from(authorizationRequest)
                    .state(encodedNewState)  // 기존 시스템과 일관성 유지를 위해 encodedNewState 사용
                    .redirectUri(authorizationRequest.getRedirectUri())
                    .additionalParameters(appleAdditionalParameters)
                    .build();
            }


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
