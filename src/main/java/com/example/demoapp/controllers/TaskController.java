package com.example.demoapp.controllers;

import com.example.demoapp.models.Task;
import com.example.demoapp.models.User;
import com.example.demoapp.repository.TaskRepository;
import com.example.demoapp.repository.UserRepository;
import com.example.demoapp.respones.TaskRequest;
import com.example.demoapp.respones.TaskResponse;
import com.example.demoapp.security.UserPrincipal;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task")
@AllArgsConstructor
public class TaskController {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @PostMapping("/create")
    public ResponseEntity<Task> createTask(@RequestBody TaskRequest req, @AuthenticationPrincipal UserPrincipal principal){
        Task task = new Task();
        var user = userRepository.findByUsername(principal.getUsername()).orElseThrow();
        task.setName(req.getName());
        task.setDescription(req.getDescription());
        task.setFinished(req.getFinished());
        task.setUser(user);

        taskRepository.save(task);
        return new ResponseEntity<>(Task.builder().name(req.getName()).description(req.getDescription()).build(), HttpStatus.CREATED);
    }

    @GetMapping("/getAll")
    public List<Task> getAll(@AuthenticationPrincipal UserPrincipal principal){

        return taskRepository.findByUserUsername(principal.getUsername());
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<TaskResponse> getById(@PathVariable("id") Integer id, @AuthenticationPrincipal UserPrincipal principal){
        Task task = taskRepository.findById(id).orElseThrow();
        User taskUser = task.getUser();
        String username = taskUser.getUsername();

        // If the task belongs to the user, return it
        if(username.equals(principal.getUsername())){
            TaskResponse response = TaskResponse.builder().task(task).message("Task found.").build();
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        TaskResponse response = TaskResponse.builder().message("Could not find the task.").build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<TaskResponse> deleteTask(@PathVariable("id") Integer id, @AuthenticationPrincipal UserPrincipal principal){
        Task task = taskRepository.findById(id).orElseThrow();
        User taskUser = task.getUser();
        String username = taskUser.getUsername();

        // If the task belongs to the user, delete and return it
        if(username.equals(principal.getUsername())){
            taskRepository.deleteById(id);
            TaskResponse response = TaskResponse.builder().task(task).message("Task deleted.").build();
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }

        TaskResponse response = TaskResponse.builder().message("Could not find and delete.").build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }


    @PatchMapping("/update/{id}")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable("id") Integer id, @RequestBody TaskRequest req, @AuthenticationPrincipal UserPrincipal principal){
        Task task = taskRepository.findById(id).orElseThrow();
        User taskUser = task.getUser();
        String username = taskUser.getUsername();

        // If the task belongs to the user, delete and return it
        if(username.equals(principal.getUsername())){
            task.setName(req.getName());
            task.setDescription(req.getDescription());
            task.setFinished(req.getFinished());
            taskRepository.save(task);
            TaskResponse response = TaskResponse.builder().task(task).message("Task updated.").build();
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }

        TaskResponse response = TaskResponse.builder().message("Could not find and update!").build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

}
