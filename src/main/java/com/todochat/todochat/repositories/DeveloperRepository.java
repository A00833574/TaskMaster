package com.todochat.todochat.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.todochat.todochat.models.Developer;

public interface DeveloperRepository extends JpaRepository<Developer, Integer> {
    Developer findByMail(String mail);
    
}

