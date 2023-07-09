package com.example.demoapp.respones;

import com.example.demoapp.models.Task;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class LoginResponse {
    private final String accessToken;
    private final String message;
}
