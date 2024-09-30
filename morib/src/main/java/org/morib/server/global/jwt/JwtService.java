package org.morib.server.global.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.morib.server.domain.user.infra.UserRepository;
import org.morib.server.global.exception.NotFoundException;
import org.morib.server.global.exception.UnauthorizedException;
import org.morib.server.global.message.ErrorMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

import static org.morib.server.global.common.Constants.*;

@Service
@RequiredArgsConstructor
@Getter
@Slf4j
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;
    @Value("${jwt.expiration.access-token}")
    private Long accessTokenExpirationPeriod;
    @Value("${jwt.expiration.refresh-token}")
    private Long refreshTokenExpirationPeriod;
    @Value("${jwt.header.access-token}")
    private String accessHeader;
    @Value("${jwt.header.refresh-token}")
    private String refreshHeader;

    private final UserRepository userRepository;

    public String createAccessToken(Long id) {
        final Claims claims = getClaimsWithId(id, accessTokenExpirationPeriod, ACCESS_TOKEN_SUBJECT);
        return generateJwt(claims);
   }

    public String createRefreshToken() {
        final Claims claims = getClaims(refreshTokenExpirationPeriod, REFRESH_TOKEN_SUBJECT);
        return generateJwt(claims);
    }

    private String generateJwt(Claims claims) {
        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE) // Header
                .setClaims(claims) // Claim
                .signWith(getSigningKey()) // Signature
                .compact();
    }

    private Claims getClaims(final Long tokenExpirationPeriod, final String subject) {
        final Date now = new Date();
        return Jwts.claims()
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenExpirationPeriod))
                .setSubject(subject);
    }

    private Claims getClaimsWithId(final Long id, final Long tokenExpirationPeriod, final String subject) {
        final Date now = new Date();
        Claims claims = Jwts.claims()
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenExpirationPeriod))
                .setSubject(subject);
        claims.put(ID_CLAIM, id);
        return claims;
    }

    public Optional<String> extractRefreshToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(AUTHORIZATION))
                .filter(refreshToken -> refreshToken.startsWith(BEARER))
                .map(refreshToken -> refreshToken.replace(BEARER, ""))
                .filter(this::isRefreshToken);
    }

    public Optional<String> extractAccessToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(AUTHORIZATION))
                .filter(accessToken -> accessToken.startsWith(BEARER))
                .map(accessToken -> accessToken.replace(BEARER, ""))
                .filter(this::isAccessToken);
    }

    public Optional<String> extractId(String accessToken) {
        try {
            Claims claims = extractClaimsFromToken(accessToken);
            return Optional.of(claims.get(ID_CLAIM, Integer.class).toString());

        } catch (Exception e) {
            log.error("액세스 토큰이 유효하지 않습니다.");
            return Optional.empty();
        }
    }

    public void updateRefreshToken(Long userId, String refreshToken) {
        userRepository.findById(userId)
                .ifPresentOrElse(
                        user -> user.updateRefreshToken(refreshToken),
                        () -> new NotFoundException(ErrorMessage.NOT_FOUND)
                );
    }

    public boolean isTokenValid(String token) {
        try {
            extractClaimsFromToken(token);
            return true;
        } catch (Exception e) {
            throw new UnauthorizedException(ErrorMessage.INVALID_TOKEN);
        }
    }

    private Claims extractClaimsFromToken(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(getSigningKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    private SecretKey getSigningKey() {
        String encodedKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        return Keys.hmacShaKeyFor(encodedKey.getBytes());
    }

    private String getTokenType(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    private boolean isAccessToken(String token) {
        String tokenType = getTokenType(token);
        if (ACCESS_TOKEN_SUBJECT.equals(tokenType)) {
            return true;
        }
        else throw new UnauthorizedException(ErrorMessage.INVALID_TOKEN);
    }

    private boolean isRefreshToken(String token) {
        String tokenType = getTokenType(token);
        if (REFRESH_TOKEN_SUBJECT.equals(tokenType)) {
            return true;
        }
        else throw new UnauthorizedException(ErrorMessage.INVALID_TOKEN);
    }
}

