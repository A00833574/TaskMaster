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
import com.todochat.todochat.models.Task;
import com.todochat.todochat.repositories.DeveloperRepository;
import com.todochat.todochat.services.DeveloperService;

public class DeveloperServiceTest {

    @Mock
    private DeveloperRepository developerRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private DeveloperService developerService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateDeveloper() {
        Developer developer = new Developer();
        developer.setPassword("password");
        developer.setId(1);

        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(developerRepository.save(any(Developer.class))).thenReturn(developer);

        Developer createdDeveloper = developerService.createDeveloper(developer);

        assertNotNull(createdDeveloper);
        assertEquals("encodedPassword", createdDeveloper.getPassword());
        verify(passwordEncoder).encode("password");
        verify(developerRepository).save(developer);
    }

    @Test
    public void testGetAllDevelopers() {
        Developer developer1 = new Developer();
        Developer developer2 = new Developer();
        List<Developer> developers = Arrays.asList(developer1, developer2);

        when(developerRepository.findAll()).thenReturn(developers);

        List<Developer> result = developerService.getAllDevelopers();

        assertEquals(2, result.size());
        verify(developerRepository).findAll();
    }

    @Test
    public void testGetDeveloperById() {
        Developer developer = new Developer();
        developer.setId(1000);
        when(developerRepository.findById(anyInt())).thenReturn(Optional.of(developer));

        Developer result = developerService.getDeveloperById(1000);

        assertEquals(1000, result.getId());
        verify(developerRepository).findById(1000);
    }

    @Test
    public void testDeleteDeveloperById() {
        String result = developerService.deleteDeveloperById(1000);

        assertEquals("Developer with id 1000 has been deleted", result);
        verify(developerRepository).deleteById(1000);
    }

    @Test
    public void testUpdateDeveloper() {
        Developer developer = new Developer();
        developer.setId(1000);
        developer.setName("Updated test");
        developer.setLastname("Updated lastname");
        developer.setPassword("password2");
        developer.setMail("testUpdate@mail.com");
        developer.setPhone("8111111111");
        developer.setRole("Developer");

        Developer existingDeveloper = new Developer();
        existingDeveloper.setId(1000);

        when(developerRepository.findById(anyInt())).thenReturn(Optional.of(existingDeveloper));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword2");
        when(developerRepository.save(any(Developer.class))).thenReturn(existingDeveloper);

        Developer updatedDeveloper = developerService.updateDeveloper(developer);

        assertNotNull(updatedDeveloper);
        assertEquals("Updated test", updatedDeveloper.getName());
        assertEquals("Updated lastname", updatedDeveloper.getLastname());
        assertEquals("encodedPassword2", updatedDeveloper.getPassword());
        assertEquals("testUpdate@mail.com", updatedDeveloper.getMail());
        assertEquals(1000, updatedDeveloper.getId());
        assertEquals("8111111111", updatedDeveloper.getPhone());
        assertEquals("Developer", updatedDeveloper.getRole());
        verify(developerRepository).findById(1000);
        verify(passwordEncoder).encode("password2");
        verify(developerRepository).save(existingDeveloper);
    }

    @Test
    public void testGetTasksByDeveloperId() {
        Developer developer = new Developer();
        Task task1 = new Task();
        Task task2 = new Task();
        developer.setTareas(Arrays.asList(task1, task2));

        when(developerRepository.findById(anyInt())).thenReturn(Optional.of(developer));

        List<Task> tasks = developerService.getTasksByDeveloperId(1);

        assertEquals(2, tasks.size());
        verify(developerRepository).findById(1);
    }
}