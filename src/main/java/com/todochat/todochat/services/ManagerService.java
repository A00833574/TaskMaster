package com.todochat.todochat.services;

import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.todochat.todochat.models.Manager;
import com.todochat.todochat.repositories.ManagerRepository;

@Service
public class ManagerService {

    private final ManagerRepository managerRepository;
    private final PasswordEncoder passwordEncoder;

    
    public ManagerService(ManagerRepository ManagerRepository,PasswordEncoder passwordEncoder) {
        this.managerRepository = ManagerRepository;
        this.passwordEncoder = passwordEncoder;
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
}
