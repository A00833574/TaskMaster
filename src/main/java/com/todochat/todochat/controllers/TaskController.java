package com.todochat.todochat.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.todochat.todochat.models.Task;
import com.todochat.todochat.models.app.ServerResponse;
import com.todochat.todochat.services.TaskService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/task")
public class TaskController {
    
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // Crear una nueva Tarea
    // POST /task
    @PostMapping()
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        Task newTask = taskService.createTask(task);
        return new ResponseEntity<>(newTask, HttpStatus.CREATED);
    }

    // Obtener todas las Tareas
    // GET /task
    @GetMapping()
    public ResponseEntity<List<Task>> getAllTasks() {
        List<Task> tasks = taskService.getAllTasks();
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    // Obtener una Tarea por id
    // GET /task/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Integer id) {
        Task task = taskService.getTaskById(id);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    // Eliminar una Tarea por id
    // DELETE /task/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<ServerResponse> deleteTaskById(@PathVariable Integer id) {
        return new ResponseEntity<>(new ServerResponse(taskService.deleteTaskById(id)), HttpStatus.OK);
    }

    // Actualizar una Tarea
    // PUT /task
    @PutMapping()
    public ResponseEntity<Task> updateTask(@RequestBody Task task) {
        Task updatedTask = taskService.updateTask(task);
        return new ResponseEntity<>(updatedTask, HttpStatus.OK);
    }
}
