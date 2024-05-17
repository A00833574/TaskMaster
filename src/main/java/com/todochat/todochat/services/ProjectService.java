package com.todochat.todochat.services;

import java.util.List;
import org.springframework.stereotype.Service;

import com.todochat.todochat.models.Project;
import com.todochat.todochat.repositories.ProjectRepository;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Project createProject(Project project) {
        return projectRepository.save(project);
    }

    public Project getProjectById(Integer id) {
        return projectRepository.findById(id).orElse(null);
    }

    public String deleteProjectById(Integer id) {
        projectRepository.deleteById(id);
        return "Project with id " + id + " has been deleted";
    }

    public Project updateProject(Project project) {
        Project existingProject = projectRepository.findById(project.getId()).orElse(null);
        existingProject.setName(project.getName());
        existingProject.setDevelopers(project.getDevelopers());
        existingProject.setManager(project.getManager());
        return projectRepository.save(existingProject);
    }
    
    public Project getProjectByManagerId(int managerId) {
        return projectRepository.findByManagerId(managerId);
    }

    
}
