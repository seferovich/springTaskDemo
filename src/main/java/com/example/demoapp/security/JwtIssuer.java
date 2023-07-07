package com.example.demoapp.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Controller
@RequiredArgsConstructor
public class JwtIssuer {
    private final JwtProperties properties;

    public String issue(String username){
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(Instant.now().plus(Duration.of(1, ChronoUnit.DAYS)))
                .sign(Algorithm.HMAC256(properties.getSecretKey()));

    }

}
