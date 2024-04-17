package com.todochat.todochat.services;

import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.todochat.todochat.models.Developer;
import com.todochat.todochat.repositories.DeveloperRepository;

@Service
public class DeveloperService {
    private final DeveloperRepository developerRepository;
    private final PasswordEncoder passwordEncoder;

    public DeveloperService(DeveloperRepository DeveloperRepository,PasswordEncoder passwordEncoder) {
        this.developerRepository = DeveloperRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Developer createDeveloper(Developer developer) {
        String hashedPassword = passwordEncoder.encode(developer.getPassword());
        developer.setPassword(hashedPassword);

        return developerRepository.save(developer);
    }

    public List<Developer> getAllDevelopers() {
        return developerRepository.findAll();
    }

    public Developer getDeveloperById(Integer id) {
        return developerRepository.findById(id).orElse(null);
    }
   
    public String deleteDeveloperById(Integer id) {
        developerRepository.deleteById(id);
        return "Developer with id " + id + " has been deleted";
    }

    public Developer updateDeveloper(Developer developer) {
        Developer existingDeveloper = developerRepository.findById(developer.getId()).orElse(null);
        existingDeveloper.setName(developer.getName());
        existingDeveloper.setLastname(developer.getLastname());
        existingDeveloper.setMail(developer.getMail());
        existingDeveloper.setPassword(passwordEncoder.encode(developer.getPassword()));
        existingDeveloper.setPhone(developer.getPhone());
        existingDeveloper.setRole(developer.getRole());
        
        return developerRepository.save(existingDeveloper);
    }
}
