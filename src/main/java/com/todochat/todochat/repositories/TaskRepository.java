package com.todochat.todochat.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.todochat.todochat.models.Task;

public interface TaskRepository extends JpaRepository<Task, Integer> {
    List<Task> findAllBydeveloperId(Integer developerId);
    List<Task> findAllByproyectId(Integer developerId);
}

