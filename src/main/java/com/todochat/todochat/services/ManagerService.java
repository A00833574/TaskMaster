package com.todochat.todochat.services;

import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.todochat.todochat.models.Manager;
import com.todochat.todochat.models.Developer;
import com.todochat.todochat.models.Proyect;
import com.todochat.todochat.models.Task;
import com.todochat.todochat.repositories.ManagerRepository;
import com.todochat.todochat.repositories.ProyectRepository;
import com.todochat.todochat.repositories.DeveloperRepository;

@Service
public class ManagerService {

    private final ManagerRepository managerRepository;
    private final ProyectRepository proyectRepository;
    private final DeveloperRepository developerRepository;
    private final PasswordEncoder passwordEncoder;

    
    public ManagerService(ManagerRepository ManagerRepository, ProyectRepository proyectRepository, DeveloperRepository developerRepository , PasswordEncoder passwordEncoder) {
        this.managerRepository = ManagerRepository;
        this.passwordEncoder = passwordEncoder;
        this.proyectRepository = proyectRepository;
        this.developerRepository = developerRepository;
    }

    public Manager createManager(Manager manager) {
        String hashedPassword = passwordEncoder.encode(manager.getPassword());
        manager.setPassword(hashedPassword);

        return managerRepository.save(manager);
    }

    public List<Manager> getAllManagers() {
        return managerRepository.findAll();
    }

    public Manager getManagerById(Integer id) {
        return managerRepository.findById(id).orElse(null);
    }

    public String deleteManagerById(Integer id) {
        managerRepository.deleteById(id);
        return "Manager with id " + id + " has been deleted";
    }

    public Manager updateManager(Manager manager) {
        Manager existingManager = managerRepository.findById(manager.getId()).orElse(null);
        existingManager.setName(manager.getName());
        existingManager.setLastname(manager.getLastname());
        existingManager.setMail(manager.getMail());
        existingManager.setPassword(passwordEncoder.encode(manager.getPassword()));
        existingManager.setPhone(manager.getPhone());
        existingManager.setRole(manager.getRole());
        
        return managerRepository.save(existingManager);
    }

    public List<Task> getTasksByProyectId(Integer id) {
        Proyect proyect = proyectRepository.findById(id).orElse(null);
        return proyect.getTasks();
    }

    public String assignProyect(int idProyect, int idDeveloper) {
        Proyect existingProyect = proyectRepository.findById(idProyect).orElse(null);
        Developer existingDeveloper = developerRepository.findById(idDeveloper).orElse(null);
        existingDeveloper.setProyect(existingProyect);
        developerRepository.save(existingDeveloper);
        return "Developer with id " + idDeveloper + " has been assigned to Proyect with id " + idProyect;
    }

    public String removeDeveloperFromProject(int projectId, int developerId) {
        Developer developer = developerRepository.findById(developerId).orElse(null);
        if (developer == null) {
            return "No se encontro developer.";
        }
        if (developer.getProyect() == null || developer.getProyect().getId() != projectId) {
            return "El developer no esta asignado a este proyecto";
        }
        String developerName = developer.getName() + " " + developer.getLastname();
        String projectName = developer.getProyect().getName();
        developer.setProyect(null);
        developerRepository.save(developer);
        return String.format("Developer %s has been successfully removed from the project %s.", developerName, projectName);
    }

}
