package org.morib.server.global.oauth2.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class OAuth2LoginFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("소셜 로그인 실패! 서버 로그를 확인해주세요.");
        
        // 상세한 에러 로깅
        log.error("소셜 로그인에 실패했습니다.");
        log.error("에러 메시지: {}", exception.getMessage());
        log.error("에러 타입: {}", exception.getClass().getSimpleName());
        log.error("Request URI: {}", request.getRequestURI());
        log.error("Request Parameters: {}", request.getParameterMap());
        
        // authorization_request_not_found 에러인지 확인
        if (exception.getMessage() != null && exception.getMessage().contains("authorization_request_not_found")) {
            log.error("Authorization request not found 에러가 발생했습니다. 세션 또는 state 불일치 문제일 수 있습니다.");
            log.error("현재 세션 ID: {}", request.getSession(false) != null ? request.getSession(false).getId() : "세션 없음");
        }
        
        log.error("에러 스택 트레이스:", exception);
    }
}