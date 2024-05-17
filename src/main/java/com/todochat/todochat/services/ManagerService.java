package com.todochat.todochat.services;

import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.todochat.todochat.models.Manager;
import com.todochat.todochat.models.Developer;
import com.todochat.todochat.models.Project;
import com.todochat.todochat.models.Task;
import com.todochat.todochat.repositories.ManagerRepository;
import com.todochat.todochat.repositories.ProjectRepository;
import com.todochat.todochat.repositories.DeveloperRepository;

@Service
public class ManagerService {

    private final ManagerRepository managerRepository;
    private final ProjectRepository projectRepository;
    private final DeveloperRepository developerRepository;
    private final PasswordEncoder passwordEncoder;

    
    public ManagerService(ManagerRepository ManagerRepository, ProjectRepository projectRepository, DeveloperRepository developerRepository , PasswordEncoder passwordEncoder) {
        this.managerRepository = ManagerRepository;
        this.passwordEncoder = passwordEncoder;
        this.projectRepository = projectRepository;
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

    public List<Task> getTasksByProjectId(Integer id) {
        Project project = projectRepository.findById(id).orElse(null);
        return project.getTasks();
    }

    public String assignProject(int idProject, int idDeveloper) {
        Project existingProject = projectRepository.findById(idProject).orElse(null);
        Developer existingDeveloper = developerRepository.findById(idDeveloper).orElse(null);
        existingDeveloper.setProject(existingProject);
        developerRepository.save(existingDeveloper);
        return "Developer with id " + idDeveloper + " has been assigned to Project with id " + idProject;
    }

    public List<Developer> getDevelopersByProjectId(Integer id) {
        return developerRepository.findAllByprojectId(id);
    }
    public String removeDeveloperFromProject(int projectId, int developerId) {
        Developer developer = developerRepository.findById(developerId).orElse(null);
        if (developer == null) {
            return "No se encontro developer.";
        }
        if (developer.getProject() == null || developer.getProject().getId() != projectId) {
            return "El developer no esta asignado a este proyecto";
        }
        String developerName = developer.getName() + " " + developer.getLastname();
        String projectName = developer.getProject().getName();
        developer.setProject(null);
        developerRepository.save(developer);
        return String.format("El desarrollador %s ha sido eliminado del proyecto %s.", developerName, projectName);
    }

}
