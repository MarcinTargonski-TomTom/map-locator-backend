package com.tomtom.locator.map.map_locator.mok.controller;

import com.tomtom.locator.map.map_locator.mok.dto.NewAccountDto;
import com.tomtom.locator.map.map_locator.mok.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
class AccountController {

    private final AccountService accountService;

    @PostMapping("/register")
    ResponseEntity<Void> register(@RequestBody @Valid NewAccountDto requestBodyContent) {
        accountService.create(requestBodyContent);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
