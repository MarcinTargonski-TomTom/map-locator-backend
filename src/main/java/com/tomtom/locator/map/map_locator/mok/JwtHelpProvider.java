package com.tomtom.locator.map.map_locator.mok;

import com.tomtom.locator.map.map_locator.mok.config.JwtProperties;
import com.tomtom.locator.map.map_locator.mok.exception.ExpiredTokenException;
import com.tomtom.locator.map.map_locator.mok.exception.InvalidTokenException;
import com.tomtom.locator.map.map_locator.mok.model.Account;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
class JwtHelpProvider implements JwtHelper {

    private final JwtProperties authTokenProps;

    JwtHelpProvider(@NonNull JwtProperties authTokenProps) {
        this.authTokenProps = authTokenProps;
    }

    @Override
    public String generateAuthTokenForAnAccount(Account account) {
        String subject = account.getUsername();
        Map<String, Object> claims = prepareAuthTokenClaims(account);
        Instant issuedAt = Instant.now();
        Instant expirationAt = Instant.now().plusMillis(authTokenProps.timeoutInMillis());
        Key key = getSigningKeyFromBase64EncodedSecret();

        return generateToken(subject, claims, issuedAt, expirationAt, key, SignatureAlgorithm.HS256);
    }

    @Override
    public Map<String, Object> validateAndExtract(@NonNull String token) {
        return validateAndExtractClaimsFromJwtToken(token, getSigningKeyFromBase64EncodedSecret());
    }



    private String generateToken(String subject, Map<String, Object> claims, Instant issuedAt, Instant expirationAt, Key key, SignatureAlgorithm alg) {
        return Jwts.builder()
                .setSubject(subject)
                .addClaims(claims)
                .setIssuedAt(Date.from(issuedAt))
                .setExpiration(Date.from(expirationAt))
                .signWith(key, alg)
                .compact();
    }

    private Map<String, Object> prepareAuthTokenClaims(Account account) {
        List<String> roles = account
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return Map.ofEntries(Map.entry("accountRoles", roles));
    }

    private Key getSigningKeyFromBase64EncodedSecret() {
        String secret = authTokenProps.key();
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Claims validateAndExtractClaimsFromJwtToken(String token, Key key) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

        } catch (ExpiredJwtException e) {
            log.info("Jwt token expired", e);
            throw ExpiredTokenException.withDefaultMsgAndCause(e);

        } catch (JwtException e) {
            log.warn("Jwt token invalid", e);
            throw InvalidTokenException.withDefaultMsgAndCause(e);
        }
    }

}
