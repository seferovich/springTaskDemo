package com.example.demoapp.repository;

import com.example.demoapp.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Integer> {
    List<Task> findByUserId(Integer userId);
    @Query("select t from Task t where t.user.username = :username")
    List<Task> findByUserUsername(String username);
}
