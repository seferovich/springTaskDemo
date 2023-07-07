package com.example.demoapp.respones;

import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class RegisterRequest {
    private final String email;
    private final String username;
    private final String password;

}

