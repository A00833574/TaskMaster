package com.todochat.todochat.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.todochat.todochat.models.Proyect;
import com.todochat.todochat.models.app.ServerResponse;
import com.todochat.todochat.services.ProyectService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/proyect")
public class ProyectController {
    
    private final ProyectService proyectService;

    public ProyectController(ProyectService proyectService) {
        this.proyectService = proyectService;
    }

    // Crear un nuevo Proyecto
    // POST /proyect
    @PostMapping()
    public ResponseEntity<Proyect> createProyect(@RequestBody Proyect proyect) {
        Proyect newProyect = proyectService.createProyect(proyect);
        return new ResponseEntity<>(newProyect, HttpStatus.CREATED);
    }

    // Obtener todos los Proyectos
    // GET /proyect
    @GetMapping()
    public ResponseEntity<List<Proyect>> getAllProyects() {
        List<Proyect> proyects = proyectService.getAllProyects();
        return new ResponseEntity<>(proyects, HttpStatus.OK);
    }

    // Obtener un Proyecto por id
    // GET /proyect/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Proyect> getProyectById(@PathVariable Integer id) {
        Proyect proyect = proyectService.getProyectById(id);
        return new ResponseEntity<>(proyect, HttpStatus.OK);
    }

    // Eliminar un Proyecto por id
    // DELETE /proyect/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<ServerResponse> deleteProyectById(@PathVariable Integer id) {
        return new ResponseEntity<>(new ServerResponse(proyectService.deleteProyectById(id)), HttpStatus.OK);
    }

    // Actualizar un Proyecto
    // PUT /proyect
    @PutMapping()
    public ResponseEntity<Proyect> updateProyect(@RequestBody Proyect proyect) {
        Proyect updatedProyect = proyectService.updateProyect(proyect);
        return new ResponseEntity<>(updatedProyect, HttpStatus.OK);
    }
}