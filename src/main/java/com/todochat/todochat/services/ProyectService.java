package com.todochat.todochat.services;

import java.util.List;
import org.springframework.stereotype.Service;

import com.todochat.todochat.models.Proyect;
import com.todochat.todochat.repositories.ProyectRepository;

@Service
public class ProyectService {
    private final ProyectRepository proyectRepository;

    public ProyectService(ProyectRepository proyectRepository) {
        this.proyectRepository = proyectRepository;
    }

    public List<Proyect> getAllProyects() {
        return proyectRepository.findAll();
    }

    public Proyect createProyect(Proyect proyect) {
        return proyectRepository.save(proyect);
    }

    public Proyect getProyectById(Integer id) {
        return proyectRepository.findById(id).orElse(null);
    }

    public String deleteProyectById(Integer id) {
        proyectRepository.deleteById(id);
        return "Proyect with id " + id + " has been deleted";
    }

    public Proyect updateProyect(Proyect proyect) {
        Proyect existingProyect = proyectRepository.findById(proyect.getId()).orElse(null);
        existingProyect.setName(proyect.getName());
        existingProyect.setDevelopers(proyect.getDevelopers());
        existingProyect.setManager(proyect.getManager());
        return proyectRepository.save(existingProyect);
    }
    
    public Proyect getProyectByManagerId(int managerId) {
        return proyectRepository.findByManagerId(managerId);
    }

    
}
