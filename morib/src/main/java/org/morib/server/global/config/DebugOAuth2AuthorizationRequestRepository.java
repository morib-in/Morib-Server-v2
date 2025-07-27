package org.morib.server.global.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;

@Slf4j
public class DebugOAuth2AuthorizationRequestRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    private final HttpSessionOAuth2AuthorizationRequestRepository delegate = new HttpSessionOAuth2AuthorizationRequestRepository();

    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        log.info("=== loadAuthorizationRequest DEBUG START ===");
        log.info("Request URI: {}", request.getRequestURI());
        log.info("Request Method: {}", request.getMethod());
        log.info("Session ID: {}", request.getSession(false) != null ? request.getSession(false).getId() : "NO SESSION");
        
        // 1. State 파라미터 확인
        String stateParameter = request.getParameter(OAuth2ParameterNames.STATE);
        log.info("1. State parameter from request: {}", stateParameter);
        
        if (stateParameter == null) {
            log.error("❌ State parameter is NULL - returning null");
            return null;
        }
        
        // 2. 실제 delegate에서 Authorization Request 찾기
        OAuth2AuthorizationRequest authorizationRequest = delegate.loadAuthorizationRequest(request);
        log.info("2. Authorization request from session: {}", authorizationRequest != null ? "FOUND" : "NULL");
        
        if (authorizationRequest == null) {
            log.error("❌ Authorization request NOT FOUND in session - returning null");
            return null;
        }
        
        // 3. State 비교
        String sessionState = authorizationRequest.getState();
        log.info("3. State comparison:");
        log.info("   Request state: {}", stateParameter);
        log.info("   Session state: {}", sessionState);
        log.info("   States equal: {}", stateParameter.equals(sessionState));
        
        boolean statesMatch = stateParameter.equals(sessionState);
        if (!statesMatch) {
            log.error("❌ State MISMATCH - returning null");
            log.error("   Request state length: {}", stateParameter.length());
            log.error("   Session state length: {}", sessionState.length());
            // 첫 50자만 비교 로깅
            log.error("   Request state (first 50): {}", stateParameter.length() > 50 ? stateParameter.substring(0, 50) + "..." : stateParameter);
            log.error("   Session state (first 50): {}", sessionState.length() > 50 ? sessionState.substring(0, 50) + "..." : sessionState);
            return null;
        }
        
        log.info("✅ All checks passed - returning authorization request");
        return authorizationRequest;
    }

    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request, HttpServletResponse response) {
        log.info("=== saveAuthorizationRequest DEBUG ===");
        log.info("Session ID: {}", request.getSession(true).getId());
        log.info("State being saved: {}", authorizationRequest != null ? authorizationRequest.getState() : "NULL");
        log.info("Registration ID: {}", authorizationRequest != null ? authorizationRequest.getAttribute("registration_id") : "NULL");
        
        delegate.saveAuthorizationRequest(authorizationRequest, request, response);
        log.info("✅ Authorization request saved successfully");
    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request, HttpServletResponse response) {
        log.info("=== removeAuthorizationRequest DEBUG ===");
        OAuth2AuthorizationRequest result = delegate.removeAuthorizationRequest(request, response);
        log.info("Remove result: {}", result != null ? "SUCCESS" : "NULL");
        return result;
    }
} 