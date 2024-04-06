package com.todochat.todochat.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.todochat.todochat.models.Manager;

public interface ManagerRepository extends JpaRepository<Manager, Integer> {
    
}

