package com.example.demoapp.respones;


import com.example.demoapp.models.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RegisterResponse {
    private final String message;
    private final User user;
}
