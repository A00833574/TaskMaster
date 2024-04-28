package com.todochat.todochat.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.todochat.todochat.models.AuthToken;
import com.todochat.todochat.models.Developer;
import com.todochat.todochat.models.Manager;
import com.todochat.todochat.repositories.AuthTokenRepository;
import com.todochat.todochat.repositories.DeveloperRepository;
import com.todochat.todochat.repositories.ManagerRepository;

import java.util.Calendar;
import java.util.Date;


// Servicio encargado de manejar la autenticacion ya sea de los desarrolladores o de los managers
@Service
public class AuthService {

    private final DeveloperRepository developerRepository;
    private final AuthTokenRepository authTokenRepository;
    private final ManagerRepository managerRepository;
    private final PasswordEncoder passwordEncoder;


    public AuthService(DeveloperRepository developerRepository, AuthTokenRepository authTokenRepository,
            ManagerRepository managerRepository, PasswordEncoder passwordEncoder) {
        this.developerRepository = developerRepository;
        this.authTokenRepository = authTokenRepository;
        this.managerRepository = managerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // === Este metodo se encargara de hacer login a un manager
    public boolean loginManager(String chatId, String telegramUserId, String mail, String password) {
        // Buscar el manager por mail
        Manager manager = managerRepository.findByMail(mail);

        // Si no existe, retornar false
        if (manager == null) {
            return false;
        }

        // Si la contraseña no coincide, retornar false
        if (!passwordEncoder.matches(password, manager.getPassword())) {
            return false;
        }

        // Si todo esta correcto, guardar el chatId y el telegramUserId
        // Creamos una fecha de expiracion para el token en un mes
        // Obtener la fecha actual
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 1);
        Date expirationDate = calendar.getTime();
        
        // Crear el token
        AuthToken token = new AuthToken();
        token.setChatId(chatId);
        token.setTelegramUserId(telegramUserId);
        token.setFechaVencimiento(expirationDate);
        token.setManager(manager);
        // Guardamos el token
        authTokenRepository.save(token);

        return true;
    }
    // === Este metodo se encargara de hacer login a un manager
    public boolean loginDeveloper(String chatId, String telegramUserId, String mail, String password) {
        // Buscar el manager por mail
        Developer developer = developerRepository.findByMail(mail);

        // Si no existe, retornar false
        if (developer == null) {
            return false;
        }

        // Si la contraseña no coincide, retornar false
        if (!passwordEncoder.matches(password, developer.getPassword())) {
            return false;
        }

        // Si todo esta correcto, guardar el chatId y el telegramUserId
        // Creamos una fecha de expiracion para el token en un mes
        // Obtener la fecha actual
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 1);
        Date expirationDate = calendar.getTime();
        
        // Crear el token
        AuthToken token = new AuthToken();
        token.setChatId(chatId);
        token.setTelegramUserId(telegramUserId);
        token.setFechaVencimiento(expirationDate);
        token.setDeveloper(developer);
        // Guardamos el token
        authTokenRepository.save(token);

        return true;
    }

    // Funcion que va a autenticar a un usuario, ya sea manager o developer
    public AuthToken authenticate(String chatId, String telegramUserId) {
        // Buscar el token por chatId y telegramUserId
        AuthToken token = authTokenRepository.findBychatIdAndTelegramUserId(chatId, telegramUserId);

        // Si no existe, retornar false
        if (token == null) {
            return null;
        }

        // Si la fecha de vencimiento es menor a la fecha actual, retornar false
        if (token.getFechaVencimiento().before(new Date())) {
            return null;
        }
        return token;
    }
}
