package com.todochat.todochat.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.todochat.todochat.models.Developer;

public interface DeveloperRepository extends JpaRepository<Developer, Integer> {
    Developer findByMail(String mail);
    List<Developer> findAllByproyect(int id_proyect);
    
}

