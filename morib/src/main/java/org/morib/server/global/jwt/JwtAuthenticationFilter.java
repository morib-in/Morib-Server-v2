package org.morib.server.global.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.morib.server.domain.user.infra.User;
import org.morib.server.domain.user.infra.UserRepository;
import org.morib.server.global.userauth.CustomUserAuthentication;
import org.morib.server.global.userauth.CustomUserDetails;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String NO_CHECK_URL = "/login"; // "/login"으로 들어오는 요청은 Filter 작동 X
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getRequestURI().equals(NO_CHECK_URL)) {
            filterChain.doFilter(request, response);
            return;
        }
        String refreshToken = jwtService.extractRefreshToken(request)
                .filter(jwtService::isTokenValid)
                .orElse(null);

        if (refreshToken != null) {
            checkRefreshTokenAndReIssueAccessToken(response, refreshToken);
            return;
        }
        checkAccessTokenAndAuthentication(request, response, filterChain);
    }

    public void checkRefreshTokenAndReIssueAccessToken(HttpServletResponse response, String refreshToken) {
        userRepository.findByRefreshToken(refreshToken)
                .ifPresent(user -> {
                    String reIssuedRefreshToken = reIssueRefreshToken(user);
                    jwtService.sendAccessAndRefreshToken(response, jwtService.createAccessToken(user.getId()),
                            reIssuedRefreshToken);
                });
    }

    private String reIssueRefreshToken(User user) {
        String reIssuedRefreshToken = jwtService.createRefreshToken();
        user.updateRefreshToken(reIssuedRefreshToken);
        userRepository.saveAndFlush(user);
        return reIssuedRefreshToken;
    }

    public void checkAccessTokenAndAuthentication(HttpServletRequest request, HttpServletResponse response,
                                                  FilterChain filterChain) throws ServletException, IOException {
        log.info("checkAccessTokenAndAuthentication() 호출");
        jwtService.extractAccessToken(request)
                .filter(jwtService::isTokenValid)
                .ifPresent(accessToken ->
                        jwtService.extractId(accessToken)
                                .ifPresent(id -> userRepository.findById(Long.valueOf(id))
                                        .ifPresent(this::saveAuthentication)));
        filterChain.doFilter(request, response);
    }

    public void saveAuthentication(User myUser) {
        CustomUserDetails customUserDetails = CustomUserDetails.of(myUser.getId(), myUser.getEmail(), myUser.getRole().name());
        CustomUserAuthentication customUserAuthentication =
                CustomUserAuthentication.of(customUserDetails, null, authoritiesMapper.mapAuthorities(customUserDetails.getAuthorities()));
        SecurityContextHolder.getContext().setAuthentication(customUserAuthentication);
    }
}

