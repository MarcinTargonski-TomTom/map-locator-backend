package com.tomtom.locator.map.map_locator.mok.config;

import com.tomtom.locator.map.map_locator.mok.AccountService;
import com.tomtom.locator.map.map_locator.mok.exception.AccountNotActiveException;
import com.tomtom.locator.map.map_locator.mok.helper.JwtHelper;
import com.tomtom.locator.map.map_locator.mok.model.Account;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static java.util.Objects.isNull;

@Component
@RequiredArgsConstructor
@Slf4j
class JwtFilter extends OncePerRequestFilter {

    private static final String BEARER = "Bearer";

    private final JwtHelper jwtHelper;
    private final AccountService accountService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (isNull(header) || !header.startsWith(BEARER)) {
            filterChain.doFilter(request, response);
            return;
        }

        tryAuthorize(request, response, filterChain, header, BEARER);
    }

    private void tryAuthorize(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain, String header, String BEARER) throws IOException, ServletException {
        try {
            String token = header.substring(BEARER.length());
            String subject = jwtHelper.extractSubject(token);
            Account account = accountService.findByLogin(subject);

            if (!account.isEnabled()) {
                throw AccountNotActiveException.withDefaultMsg();
            }

            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(
                            account.getUsername(),
                            null,
                            account.getAuthorities()
                    )
            );

        } catch (Exception e) {
            log.warn("Exception occurred in JwtFilter: ", e);
        } finally {
            filterChain.doFilter(request, response);
        }
    }
}
