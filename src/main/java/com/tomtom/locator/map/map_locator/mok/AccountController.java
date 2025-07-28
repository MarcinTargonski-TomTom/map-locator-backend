package com.tomtom.locator.map.map_locator.mok;

import com.tomtom.locator.map.map_locator.mok.dto.NewAccountDto;
import com.tomtom.locator.map.map_locator.mok.model.Credentials;
import com.tomtom.locator.map.map_locator.mok.model.Tokens;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
class AccountController {

    private final AuthService authService;
    private final AccountService accountService;

    @PostMapping("/auth")
    ResponseEntity<Tokens> authenticate(@RequestBody @Valid Credentials credentials) {
        return ResponseEntity.ok(authService.authenticate(credentials));
    }

    @PostMapping("/register")
    ResponseEntity<Void> register(@RequestBody @Valid NewAccountDto requestBodyContent) {
        accountService.create(requestBodyContent);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
