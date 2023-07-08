package com.example.demoapp.controllers;

import com.example.demoapp.models.Task;
import com.example.demoapp.models.User;
import com.example.demoapp.repository.TaskRepository;
import com.example.demoapp.repository.UserRepository;
import com.example.demoapp.respones.LoginResponse;
import com.example.demoapp.respones.RegisterResponse;
import com.example.demoapp.respones.TaskCreateRequest;
import com.example.demoapp.respones.TaskCreateResponse;
import com.example.demoapp.security.UserPrincipal;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/task")
@AllArgsConstructor
public class TaskController {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @PostMapping("/create")
    public ResponseEntity<Task> createTask(@RequestBody TaskCreateRequest req, @AuthenticationPrincipal UserPrincipal principal){
        Task task = new Task();
        var user = userRepository.findByUsername(principal.getUsername()).orElseThrow();
        task.setName(req.getName());
        task.setDescription(req.getDescription());
        task.setUser(user);

        taskRepository.save(task);
        return new ResponseEntity<>(Task.builder().name(req.getName()).description(req.getDescription()).build(), HttpStatus.CREATED);
    }

    @GetMapping("/getAll")
    public List<Task> getAll(@AuthenticationPrincipal UserPrincipal principal){


        return taskRepository.findByUserUsername(principal.getUsername());
    }

}
