package com.example.demoapp.respones;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TaskCreateRequest {
    private final String name;
    private final String description;

}
