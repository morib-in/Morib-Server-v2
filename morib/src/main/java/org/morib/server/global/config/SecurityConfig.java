package org.morib.server.global.config;

import lombok.RequiredArgsConstructor;
import org.morib.server.domain.user.infra.UserRepository;
import org.morib.server.global.jwt.CustomJwtAuthenticationEntryPoint;
import org.morib.server.global.jwt.JwtAuthenticationFilter;
import org.morib.server.global.jwt.JwtService;
import org.morib.server.global.oauth2.handler.CustomLogoutHandler;
import org.morib.server.global.oauth2.handler.CustomLogoutSuccessHandler;
import org.morib.server.global.oauth2.handler.OAuth2LoginFailureHandler;
import org.morib.server.global.oauth2.handler.OAuth2LoginSuccessHandler;
import org.morib.server.global.oauth2.service.CustomOAuth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.RequestCacheConfigurer;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomJwtAuthenticationEntryPoint customJwtAuthenticationEntryPoint;
    private final CustomAuthorizationRequestResolver customAuthorizationRequestResolver;
    private final CustomLogoutHandler customLogoutHandler;
    private final CustomLogoutSuccessHandler customLogoutSuccessHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .requestCache(RequestCacheConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .exceptionHandling(exception -> exception.authenticationEntryPoint(customJwtAuthenticationEntryPoint))
                .logout(logout -> {
                    logout.logoutUrl("/api/v2/users/logout");
                    logout.addLogoutHandler(customLogoutHandler);
                    logout.logoutSuccessHandler(customLogoutSuccessHandler);
                })
                .oauth2Login(oauth2 -> {
                    oauth2.successHandler(oAuth2LoginSuccessHandler);
                    oauth2.failureHandler(oAuth2LoginFailureHandler);
                    oauth2.userInfoEndpoint(user -> user.userService(customOAuth2UserService));
                    oauth2.authorizationEndpoint(auth -> auth
                            .authorizationRequestRepository(new HttpSessionOAuth2AuthorizationRequestRepository())
                            .authorizationRequestResolver(customAuthorizationRequestResolver));

                });

        http.authorizeHttpRequests((auth) -> auth
                .requestMatchers("/","/css/**","/images/**","/js/**","/favicon.ico", "/profile", "/health,", "/actuator/**", "/error", "/api/v2/s3/**", "/api/v2/waiting/windows").permitAll()
                .anyRequest().authenticated()
        );
        http.addFilterAfter(jwtAuthenticationFilter(), LogoutFilter.class);
        return http.build();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtService, userRepository, customJwtAuthenticationEntryPoint);
    }

}
