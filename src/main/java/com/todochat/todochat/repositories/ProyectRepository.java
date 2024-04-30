package com.todochat.todochat.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.todochat.todochat.models.Proyect;

public interface ProyectRepository extends JpaRepository<Proyect, Integer> {
    Proyect findByManagerId(int managerId);
}

