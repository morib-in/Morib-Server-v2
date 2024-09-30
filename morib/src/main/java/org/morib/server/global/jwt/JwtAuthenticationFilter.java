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
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = jwtService.extractAccessToken(request)
                .filter(jwtService::isTokenValid)
                .orElse(null);
        handleAccessToken(accessToken);
        filterChain.doFilter(request, response);
    }

//    public void checkRefreshTokenAndReIssueAccessToken(HttpServletResponse response, String refreshToken) {
//        userRepository.findByRefreshToken(refreshToken)
//                .ifPresent(user -> {
//                    String reIssuedRefreshToken = reIssueRefreshToken(user);
//                    jwtService.sendAccessAndRefreshToken(response, jwtService.createAccessToken(user.getId()),
//                            reIssuedRefreshToken);
//                });
//    }
//
//    private String reIssueRefreshToken(User user) {
//        String reIssuedRefreshToken = jwtService.createRefreshToken();
//        user.updateRefreshToken(reIssuedRefreshToken);
//        userRepository.saveAndFlush(user);
//        return reIssuedRefreshToken;
//    }


    private void handleAccessToken(String accessToken) {
        jwtService.extractId(accessToken)
                .flatMap(id -> userRepository.findById(Long.valueOf(id)))
                .ifPresent(this::saveAuthentication);
    }

    public void saveAuthentication(User myUser) {
        CustomUserDetails customUserDetails = CustomUserDetails.of(myUser.getId(), myUser.getEmail(), myUser.getRole().name());
        CustomUserAuthentication customUserAuthentication =
                CustomUserAuthentication.of(customUserDetails, null, authoritiesMapper.mapAuthorities(customUserDetails.getAuthorities()));
        SecurityContextHolder.getContext().setAuthentication(customUserAuthentication);
    }
}

