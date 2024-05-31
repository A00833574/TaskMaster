package com.todochat.todochat;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.todochat.todochat.models.Project;
import com.todochat.todochat.repositories.ProjectRepository;
import com.todochat.todochat.services.ProjectService;

public class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ProjectService projectService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllProjects() {
        List<Project> projects = new ArrayList<>();
        projects.add(new Project());
        when(projectRepository.findAll()).thenReturn(projects);

        List<Project> result = projectService.getAllProjects();

        assertEquals(1, result.size());
        verify(projectRepository).findAll();
    }

    @Test
    public void testCreateProject() {
        Project project = new Project();
        when(projectRepository.save(any(Project.class))).thenReturn(project);

        Project result = projectService.createProject(project);

        assertNotNull(result);
        verify(projectRepository).save(project);
    }

    @Test
    public void testGetProjectById() {
        Project project = new Project();
        when(projectRepository.findById(anyInt())).thenReturn(Optional.of(project));

        Project result = projectService.getProjectById(1);

        assertNotNull(result);
        verify(projectRepository).findById(1);
    }

    @Test
    public void testDeleteProjectById() {
        String result = projectService.deleteProjectById(1);

        assertEquals("Project with id 1 has been deleted", result);
        verify(projectRepository).deleteById(1);
    }

    @Test
    public void testUpdateProject() {
        Project project = new Project();
        project.setId(1);
        when(projectRepository.findById(anyInt())).thenReturn(Optional.of(project));
        when(projectRepository.save(any(Project.class))).thenReturn(project);

        Project result = projectService.updateProject(project);

        assertNotNull(result);
        verify(projectRepository).findById(1);
        verify(projectRepository).save(project);
    }

    @Test
    public void testGetProjectByManagerId() {
        Project project = new Project();
        when(projectRepository.findByManagerId(anyInt())).thenReturn(project);

        Project result = projectService.getProjectByManagerId(1);

        assertNotNull(result);
        verify(projectRepository).findByManagerId(1);
    }
}