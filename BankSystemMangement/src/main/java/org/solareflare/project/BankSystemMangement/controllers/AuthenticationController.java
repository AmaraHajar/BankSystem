package org.solareflare.project.BankSystemMangement.controllers;

import lombok.RequiredArgsConstructor;
import org.solareflare.project.BankSystemMangement.services.AuthenticationService;
import org.solareflare.project.BankSystemMangement.dto.JwtAuthenticationResponse;
import org.solareflare.project.BankSystemMangement.dto.SignInRequest;
import org.solareflare.project.BankSystemMangement.dto.SignUpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authenticate")
@RequiredArgsConstructor
public class AuthenticationController {

    @Autowired
      private AuthenticationService authenticationService;

    @PostMapping("/signup")
    public JwtAuthenticationResponse signup(@RequestBody SignUpRequest request) {
        return authenticationService.signup(request);
    }

    @PostMapping("/signin")
    public JwtAuthenticationResponse signin(@RequestBody SignInRequest request) {
        return authenticationService.signin(request);
    }
}