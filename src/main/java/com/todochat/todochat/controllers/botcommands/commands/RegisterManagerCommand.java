package com.todochat.todochat.controllers.botcommands.commands;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import com.todochat.todochat.controllers.TaskBotController;
import com.todochat.todochat.controllers.botcommands.BotCommand;
import com.todochat.todochat.models.AuthToken;
import com.todochat.todochat.models.Manager;
import com.todochat.todochat.services.AuthService;
import com.todochat.todochat.services.ManagerService;
import com.todochat.todochat.services.TelegramService;

@Component
public class RegisterManagerCommand implements BotCommand {

    @Autowired
    private ManagerService managerService;
    @Autowired
    private AuthService authService;

    @Override
    public void executeCommand(Update update, TaskBotController botController,String[] arguments) {
        // Instanciamos el telegramService
        TelegramService telegramService = new TelegramService(update, botController);
        
        try {
            
        // Obtenemos los argumentos del manager
        String name = arguments[0];
        String lastname = arguments[1];
        String phone = arguments[2];
        String mail = arguments[3];
        String password = arguments[4];
        String role = arguments[5];

        // Creamos el manager
        Manager manager = new Manager();
        manager.setName(name);
        manager.setLastname(lastname);
        manager.setPhone(phone);
        manager.setMail(mail);
        manager.setPassword(password);
        manager.setRole(role);

        managerService.createManager(manager);

        // Creamos una autenticacion
        AuthToken authToken = new AuthToken();
        authToken.setChatId(update.getMessage().getChatId().toString());
        authToken.setManager(manager);
        authService.createAuthToken(authToken);
        
        telegramService.addRow(List.of("(CREAR PROYECTO)/createProject","(VER MI PROYECTO)/myProject"));
        telegramService.addRow(List.of("(VER DESARROLLADORES)/listDevelopers","(VER TAREAS DE PROYECTO)/projectTasks"));
        telegramService.addRow("(IR A INICIO)/start");
        telegramService.sendMessage("Haz sido registrado correctamente y te encuentras autenticado");

    } catch (Exception e) {
        telegramService.sendMessage("Error al registrar el manager"+e.getMessage());
    }

    }
    
}
