package com.tomtom.locator.map.map_locator.security.controller;

import com.tomtom.locator.map.map_locator.security.model.Credentials;
import com.tomtom.locator.map.map_locator.security.model.Tokens;
import com.tomtom.locator.map.map_locator.security.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    ResponseEntity<Tokens> authenticate(@RequestBody @Valid Credentials credentials) {
        return ResponseEntity.ok(authService.authenticate(credentials));
    }

    @GetMapping("/extend/{refreshToken}")
    ResponseEntity<Tokens> extendSession(@PathVariable String refreshToken, Authentication authentication) {
        Tokens tokens = authService.extendSession(authentication.getName(), refreshToken);
        return ResponseEntity.ok(tokens);
    }
}
