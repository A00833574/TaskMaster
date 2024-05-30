package com.todochat.todochat;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.todochat.todochat.models.Developer;
import com.todochat.todochat.models.Manager;
import com.todochat.todochat.models.Project;
import com.todochat.todochat.models.Task;
import com.todochat.todochat.repositories.DeveloperRepository;
import com.todochat.todochat.repositories.ManagerRepository;
import com.todochat.todochat.repositories.ProjectRepository;
import com.todochat.todochat.services.ManagerService;

public class ManagerServiceTests {

    @Mock
    private ManagerRepository managerRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private DeveloperRepository developerRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private ManagerService managerService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateManager() {
        Manager manager = new Manager();
        manager.setPassword("password");
        manager.setId(1);

        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(managerRepository.save(any(Manager.class))).thenReturn(manager);

        Manager createdManager = managerService.createManager(manager);

        assertNotNull(createdManager);
        assertEquals("encodedPassword", createdManager.getPassword());
        verify(passwordEncoder).encode("password");
        verify(managerRepository).save(manager);
    }

    @Test
    public void testGetAllManagers() {
        Manager manager1 = new Manager();
        Manager manager2 = new Manager();
        List<Manager> managers = Arrays.asList(manager1, manager2);

        when(managerRepository.findAll()).thenReturn(managers);

        List<Manager> result = managerService.getAllManagers();

        assertEquals(2, result.size());
        verify(managerRepository).findAll();
    }

    @Test
    public void testGetManagerById() {
        Manager manager = new Manager();
        manager.setId(1000);
        when(managerRepository.findById(anyInt())).thenReturn(Optional.of(manager));

        Manager result = managerService.getManagerById(1000);

        assertEquals(1000, result.getId());
        verify(managerRepository).findById(1000);
    }

    @Test
    public void testDeleteManagerById() {
        String result = managerService.deleteManagerById(1000);

        assertEquals("Manager with id 1000 has been deleted", result);
        verify(managerRepository).deleteById(1000);
    }

    @Test
    public void testUpdateManager() {
        Manager manager = new Manager();
        manager.setId(1000);
        manager.setName("Updated test");
        manager.setLastname("Updated lastname");
        manager.setPassword("password2");
        manager.setMail("testUpdate@mail.com");
        manager.setPhone("8111111111");
        manager.setRole("Manager");

        Manager existingManager = new Manager();
        existingManager.setId(1000);

        when(managerRepository.findById(anyInt())).thenReturn(Optional.of(existingManager));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword2");
        when(managerRepository.save(any(Manager.class))).thenReturn(existingManager);

        Manager updatedManager = managerService.updateManager(manager);

        assertNotNull(updatedManager);
        assertEquals("Updated test", updatedManager.getName());
        assertEquals("Updated lastname", updatedManager.getLastname());
        assertEquals("encodedPassword2", updatedManager.getPassword());
        assertEquals("testUpdate@mail.com", updatedManager.getMail());
        assertEquals(1000, updatedManager.getId());
        assertEquals("8111111111", updatedManager.getPhone());
        assertEquals("Manager", updatedManager.getRole());
        verify(managerRepository).findById(1000);
        verify(passwordEncoder).encode("password2");
        verify(managerRepository).save(existingManager);
    }

    @Test
    public void testGetTasksByProjectId() {
        Project project = new Project();
        Task task1 = new Task();
        Task task2 = new Task();
        project.setTasks(Arrays.asList(task1, task2));

        when(projectRepository.findById(anyInt())).thenReturn(Optional.of(project));

        List<Task> tasks = managerService.getTasksByProjectId(1);

        assertEquals(2, tasks.size());
        verify(projectRepository).findById(1);
    }

    @Test
    public void testAssignProject() {
        Project project = new Project();
        project.setId(1000);
        Developer developer = new Developer();
        developer.setId(2000);

        when(projectRepository.findById(anyInt())).thenReturn(Optional.of(project));
        when(developerRepository.findById(anyInt())).thenReturn(Optional.of(developer));
        when(developerRepository.save(any(Developer.class))).thenReturn(developer);

        String result = managerService.assignProject(1000, 2000);

        assertEquals("Developer with id 2000 has been assigned to Project with id 1000", result);
        assertEquals(project, developer.getProject());
        verify(projectRepository).findById(1000);
        verify(developerRepository).findById(2000);
        verify(developerRepository).save(developer);
    }

    @Test
    public void testGetDevelopersByProjectId() {
        Developer developer1 = new Developer();
        Developer developer2 = new Developer();
        List<Developer> developers = Arrays.asList(developer1, developer2);

        when(developerRepository.findAllByprojectId(anyInt())).thenReturn(developers);

        List<Developer> result = managerService.getDevelopersByProjectId(1);

        assertEquals(2, result.size());
        verify(developerRepository).findAllByprojectId(1);
    }

    @Test
    public void testRemoveDeveloperFromProject() {
        Developer developer = new Developer();
        developer.setId(2);
        developer.setName("John");
        developer.setLastname("Doe");
        Project project = new Project();
        project.setId(1);
        project.setName("Project Test Remove");
        developer.setProject(project);

        when(developerRepository.findById(anyInt())).thenReturn(Optional.of(developer));

        String result = managerService.removeDeveloperFromProject(1, 2);

        assertEquals("El desarrollador John Doe ha sido eliminado del proyecto Project Test Remove.", result);
        assertNull(developer.getProject());
        verify(developerRepository).findById(2);
        verify(developerRepository).save(developer);
    }
}