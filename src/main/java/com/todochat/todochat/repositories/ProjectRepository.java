package com.todochat.todochat.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.todochat.todochat.models.Project;

public interface ProjectRepository extends JpaRepository<Project, Integer> {
    
}

