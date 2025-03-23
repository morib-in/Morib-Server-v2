package org.morib.server.global.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.morib.server.domain.user.infra.User;
import org.morib.server.domain.user.infra.UserRepository;
import org.morib.server.global.exception.NotFoundException;
import org.morib.server.global.exception.UnauthorizedException;
import org.morib.server.global.message.ErrorMessage;
import org.morib.server.global.userauth.CustomUserAuthentication;
import org.morib.server.global.userauth.CustomUserDetails;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final CustomJwtAuthenticationEntryPoint customJwtAuthenticationEntryPoint;
    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestUri = request.getRequestURI();
        if (requestUri.contains("reissue")) {
            jwtService.isTokenValidWhenReissueToken(request);
        }

        try {
            String accessToken = jwtService.extractAccessToken(request)
                    .filter(jwtService::isTokenValid)
                    .orElse(null);

            if (accessToken != null) {
                handleAccessToken(accessToken);
            }

            // 인증이 성공했거나, 인증이 필요없는 경로만 다음 필터로 진행
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            // 예외 발생 시 SecurityContext에 인증 정보를 지우고 AuthenticationEntryPoint가 처리하도록 함
            SecurityContextHolder.clearContext();

            // AuthenticationEntryPoint가 처리하도록 예외를 전달
            customJwtAuthenticationEntryPoint.commence(request, response,
                    new BadCredentialsException(e.getMessage(), e));
        }
    }

    private void handleAccessToken(String accessToken) {
        jwtService.extractId(accessToken)
                .map(id -> userRepository.findById(Long.valueOf(id)).orElseThrow(
                        () -> new NotFoundException(ErrorMessage.NOT_FOUND)
                ))
                .ifPresent(this::saveAuthentication);
    }

    private void handleRefreshToken(String refreshToken) {
        jwtService.extractId(refreshToken)
                .map(id -> userRepository.findById(Long.valueOf(id)).orElseThrow(
                        () -> new NotFoundException(ErrorMessage.NOT_FOUND)
                ))
                .ifPresent(this::saveAuthentication);
    }

    public void saveAuthentication(User myUser) {
        CustomUserDetails customUserDetails = CustomUserDetails.of(myUser.getId(), myUser.getEmail(), myUser.getRole().name());
        CustomUserAuthentication customUserAuthentication =
                CustomUserAuthentication.of(customUserDetails, null, authoritiesMapper.mapAuthorities(customUserDetails.getAuthorities()));
        SecurityContextHolder.getContext().setAuthentication(customUserAuthentication);
    }
}

