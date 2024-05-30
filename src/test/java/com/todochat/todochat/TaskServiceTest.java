package com.todochat.todochat;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.todochat.todochat.models.Task;
import com.todochat.todochat.repositories.TaskRepository;
import com.todochat.todochat.services.TaskService;

public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllTasks() {
        Task task1 = new Task();
        Task task2 = new Task();
        List<Task> tasks = Arrays.asList(task1, task2);

        when(taskRepository.findAll()).thenReturn(tasks);

        List<Task> result = taskService.getAllTasks();

        assertEquals(2, result.size());
        verify(taskRepository).findAll();
    }

    @Test
    public void testGetAllTasksByDeveloper() {
        Task task1 = new Task();
        Task task2 = new Task();
        List<Task> tasks = Arrays.asList(task1, task2);

        when(taskRepository.findAllBydeveloperId(anyInt())).thenReturn(tasks);

        List<Task> result = taskService.getAllTasksByDeveloper(1);

        assertEquals(2, result.size());
        verify(taskRepository).findAllBydeveloperId(1);
    }

    @Test
    public void testGetAllTasksByProject() {
        Task task1 = new Task();
        Task task2 = new Task();
        List<Task> tasks = Arrays.asList(task1, task2);

        when(taskRepository.findAllByprojectId(anyInt())).thenReturn(tasks);

        List<Task> result = taskService.getAllTasksByProject(1);

        assertEquals(2, result.size());
        verify(taskRepository).findAllByprojectId(1);
    }

    @Test
    public void testCreateTask() {
        Task task = new Task();

        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task result = taskService.createTask(task);

        assertNotNull(result);
        verify(taskRepository).save(task);
    }

    @Test
    public void testGetTaskById() {
        Task task = new Task();

        when(taskRepository.findById(anyInt())).thenReturn(Optional.of(task));

        Task result = taskService.getTaskById(1);

        assertNotNull(result);
        verify(taskRepository).findById(1);
    }

    @Test
    public void testDeleteTaskById() {
        doNothing().when(taskRepository).deleteById(anyInt());

        String result = taskService.deleteTaskById(1);

        assertEquals("Task with id 1 has been deleted", result);
        verify(taskRepository).deleteById(1);
    }

    @Test
    public void testUpdateTask() {
        Task existingTask = new Task();
        existingTask.setId(1);

        Task updatedTask = new Task();
        updatedTask.setId(1);
        updatedTask.setName("Updated Name");

        when(taskRepository.findById(anyInt())).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenReturn(updatedTask);

        Task result = taskService.updateTask(updatedTask);

        assertNotNull(result);
        assertEquals("Updated Name", result.getName());
        verify(taskRepository).findById(1);
        verify(taskRepository).save(existingTask);
    }
}
