package com.example.demoapp.services;

import com.example.demoapp.repository.UserRepository;
import com.example.demoapp.respones.LoginResponse;
import com.example.demoapp.security.JwtIssuer;
import com.example.demoapp.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtIssuer jwtIssuer;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    public LoginResponse attemptLogin(String username, String password) {
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        var principal = (UserPrincipal) authentication.getPrincipal();

        var token = jwtIssuer.issue(username);

        return LoginResponse.builder()
                .accessToken(token)
                .build();
    }

}
