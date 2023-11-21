package dev.ronin_engineer.software_development.application.endpoint.rest;

import dev.ronin_engineer.software_development.domain.auth.dto.TokenResponse;
import dev.ronin_engineer.software_development.domain.auth.dto.request.LoginRequest;
import dev.ronin_engineer.software_development.domain.auth.service.AuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Slf4j
public class AuthController {

    @Autowired
    AuthenticationService authenticationService;

    @PostMapping("/login")
    public TokenResponse login(@RequestBody LoginRequest request) {
        return authenticationService.authenticate(request);
    }
}
