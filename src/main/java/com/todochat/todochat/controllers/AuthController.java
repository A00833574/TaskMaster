package com.todochat.todochat.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.todochat.todochat.models.AuthToken;
import com.todochat.todochat.models.Manager;
import com.todochat.todochat.services.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {
    
    private final AuthService authService;


    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // Obtener todos los AuthTokens
    // GET /auth
    @GetMapping()
    public ResponseEntity<List<AuthToken>> getAllauths() {
        List<AuthToken> auths = authService.getAllAuthTokens();
        return new ResponseEntity<>(auths, HttpStatus.OK);
    }

    // Eliminar todos los AuthTokens
    // DELETE /auth
    @DeleteMapping()
    public ResponseEntity<String> deleteAllAuths() {
        authService.deleteAllAuthTokens();
        return new ResponseEntity<>("Todas las autenticaciones eliminadas", HttpStatus.OK);
    }
}
