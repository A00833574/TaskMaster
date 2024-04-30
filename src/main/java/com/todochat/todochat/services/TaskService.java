// Task Service class to handle the business logic of the application

package com.todochat.todochat.services;

import java.util.List;
import org.springframework.stereotype.Service;

import com.todochat.todochat.models.Task;
import com.todochat.todochat.repositories.TaskRepository;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public List<Task> getAllTasksByDeveloper(Integer id) {
        return taskRepository.findAllBydeveloperId(id);
    }
    public List<Task> getAllTasksByProject(Integer id) {
        return taskRepository.findAllByproyectId(id);
    }

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    public Task getTaskById(Integer id) {
        return taskRepository.findById(id).orElse(null);
    }

    public String deleteTaskById(Integer id) {
        taskRepository.deleteById(id);
        return "Task with id " + id + " has been deleted";
    }

    public Task updateTask(Task task) {
        Task existingTask = taskRepository.findById(task.getId()).orElse(null);
        existingTask.setName(task.getName());
        existingTask.setDescription(task.getDescription());
        existingTask.setStatus(task.getStatus());
        existingTask.setFecha_inicio(task.getFecha_inicio());
        existingTask.setFecha_finalizacion(task.getFecha_finalizacion());
        existingTask.setDeveloper(task.getDeveloper());
        return taskRepository.save(existingTask);
    }
}
