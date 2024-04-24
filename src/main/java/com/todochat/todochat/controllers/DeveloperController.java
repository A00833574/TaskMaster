package com.todochat.todochat.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.todochat.todochat.models.Developer;
import com.todochat.todochat.models.app.ServerResponse;
import com.todochat.todochat.services.DeveloperService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/developer")
public class DeveloperController {
    
    private final DeveloperService developerService;

    public DeveloperController(DeveloperService developerService) {
        this.developerService = developerService;
    }


    // Crear un nuevo Developer
    // POST /developer
    @PostMapping()
    public ResponseEntity<Developer> createDeveloper(@RequestBody Developer developer) {
        Developer newDeveloper = developerService.createDeveloper(developer);
        return new ResponseEntity<>(newDeveloper, HttpStatus.CREATED);
    }

    // Obtener todos los Developers
    // GET /developer
    @GetMapping()
    public ResponseEntity<List<Developer>> getAllDevelopers() {
        List<Developer> developers = developerService.getAllDevelopers();
        return new ResponseEntity<>(developers, HttpStatus.OK);
    }

    // Obtener un Developer por id
    // GET /developer/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Developer> getDeveloperById(@PathVariable Integer id) {
        Developer developer = developerService.getDeveloperById(id);
        return new ResponseEntity<>(developer, HttpStatus.OK);
    }

    // Eliminar un Developer por id
    // DELETE /developer/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<ServerResponse> deleteDeveloperById(@PathVariable Integer id) {
        String message = developerService.deleteDeveloperById(id);
        ServerResponse response = new ServerResponse(message);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Actualizar un Developer
    // PUT /developer
    @PutMapping()
    public ResponseEntity<Developer> updateDeveloper(@RequestBody Developer developer) {
        Developer updatedDeveloper = developerService.updateDeveloper(developer);
        return new ResponseEntity<>(updatedDeveloper, HttpStatus.OK);
    }
    
}
