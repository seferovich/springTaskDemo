package com.example.demoapp.controllers;

import com.example.demoapp.respones.LoginRequest;
import com.example.demoapp.respones.LoginResponse;
import com.example.demoapp.respones.RegisterRequest;
import com.example.demoapp.respones.RegisterResponse;
import com.example.demoapp.security.UserPrincipal;
import com.example.demoapp.services.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/auth")
@RestController
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public RegisterResponse registerUser(@RequestBody RegisterRequest req){

        return authService.attemptRegister(req.getUsername(), req.getPassword(), req.getEmail());
    }

    @PostMapping("/login")
    public LoginResponse loginUser(@RequestBody @Validated LoginRequest req){
        return authService.attemptLogin(req.getUsername(), req.getPassword());
    }

    @GetMapping("/secured")
    public String secured(@AuthenticationPrincipal @Validated UserPrincipal principal){
        return "if u see dis ur auth" + principal.getUsername();
    }
}
