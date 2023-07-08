package com.example.demoapp.respones;

import com.example.demoapp.models.Task;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TaskCreateResponse {
    private final Task task;
}
