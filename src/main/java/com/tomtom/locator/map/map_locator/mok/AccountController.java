package com.tomtom.locator.map.map_locator.mok;

import com.tomtom.locator.map.map_locator.mok.dto.NewAccountDto;
import com.tomtom.locator.map.map_locator.mok.model.Credentials;
import com.tomtom.locator.map.map_locator.mok.model.Tokens;
import jakarta.validation.Valid;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
class AccountController {

    private final AuthService authService;
    private final AccountService accountService;

    AccountController(@NonNull AuthService authService, @NonNull AccountService accountService) {
        this.authService = authService;
        this.accountService = accountService;
    }

    @PostMapping("/auth")
    @ResponseStatus(HttpStatus.OK)
    Tokens authenticate(@RequestBody @Valid Credentials credentials) {
        return authService.authenticate(credentials);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    void register(@RequestBody @Valid NewAccountDto requestBodyContent) {
        accountService.create(requestBodyContent);
    }
}
