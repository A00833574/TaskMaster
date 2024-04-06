package com.todochat.todochat.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.todochat.todochat.models.Manager;
import com.todochat.todochat.models.app.ServerResponse;
import com.todochat.todochat.services.ManagerService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/manager")
public class ManagerController {
    
    private final ManagerService managerService;

    public ManagerController(ManagerService managerService) {
        this.managerService = managerService;
    }


    // Crear un nuevo Manager
    // POST /manager
    @PostMapping()
    public ResponseEntity<Manager> createManager(@RequestBody Manager manager) {
        Manager newManager = managerService.createManager(manager);
        return new ResponseEntity<>(newManager, HttpStatus.CREATED);
    }

    // Obtener todos los Managers
    // GET /manager
    @GetMapping()
    public ResponseEntity<List<Manager>> getAllManagers() {
        List<Manager> managers = managerService.getAllManagers();
        return new ResponseEntity<>(managers, HttpStatus.OK);
    }

    // Obtener un Manager por id
    // GET /manager/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Manager> getManagerById(@PathVariable Integer id) {
        Manager manager = managerService.getManagerById(id);
        return new ResponseEntity<>(manager, HttpStatus.OK);
    }

    // Eliminar un Manager por id
    // DELETE /manager/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<ServerResponse> deleteManagerById(@PathVariable Integer id) {
        String message = managerService.deleteManagerById(id);
        ServerResponse response = new ServerResponse(message);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Actualizar un Manager
    // PUT /manager
    @PutMapping()
    public ResponseEntity<Manager> updateManager(@RequestBody Manager manager) {
        Manager updatedManager = managerService.updateManager(manager);
        return new ResponseEntity<>(updatedManager, HttpStatus.OK);
    }
    

}
