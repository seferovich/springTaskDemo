package com.example.demoapp.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;

@Component
public class JwtToPrincipalConverter {
    public UserPrincipal convert(DecodedJWT jwt){
        return UserPrincipal.builder().username(jwt.getSubject()).build();
    }
}
