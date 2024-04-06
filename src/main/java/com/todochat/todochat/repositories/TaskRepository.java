package com.todochat.todochat.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.todochat.todochat.models.Task;

public interface TaskRepository extends JpaRepository<Task, Integer> {
    
}

