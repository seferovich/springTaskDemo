package com.example.demoapp.controllers;

import com.example.demoapp.models.User;
import com.example.demoapp.repository.UserRepository;
import com.example.demoapp.respones.LoginRequest;
import com.example.demoapp.respones.LoginResponse;
import com.example.demoapp.respones.RegisterRequest;
import com.example.demoapp.respones.RegisterResponse;
import com.example.demoapp.security.JwtIssuer;
import com.example.demoapp.security.UserPrincipal;
import com.example.demoapp.services.AuthService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequestMapping
@RestController
@AllArgsConstructor
public class AuthController {
    private final JwtIssuer jwtIssuer;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;

    @PostMapping("/register")
    public RegisterResponse registerUser(@RequestBody RegisterRequest req){
        var token = jwtIssuer.issue(req.getUsername());
        User savedUser = new User();
        savedUser.setEmail(req.getEmail());
        savedUser.setPassword(passwordEncoder.encode(req.getPassword()));
        savedUser.setUsername(req.getUsername());

        userRepository.save(savedUser);

        authService.attemptLogin(req.getUsername(), req.getPassword());
        return RegisterResponse.builder().accessToken(token).user(savedUser).message("User created!").build();
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
