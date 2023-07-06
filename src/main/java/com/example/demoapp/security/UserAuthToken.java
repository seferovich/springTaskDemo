package com.example.demoapp.security;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AbstractAuthenticationToken;

public class UserAuthToken extends AbstractAuthenticationToken {
    private final UserPrincipal user;

    public UserAuthToken(UserPrincipal user) {
        super(user.getAuthorities());
        this.user = user;
        setAuthenticated(true);
    }
    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return user;
    }
}
