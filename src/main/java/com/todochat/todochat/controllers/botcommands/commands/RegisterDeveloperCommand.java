package com.todochat.todochat.controllers.botcommands.commands;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import com.todochat.todochat.controllers.TaskBotController;
import com.todochat.todochat.controllers.botcommands.BotCommand;
import com.todochat.todochat.models.AuthToken;
import com.todochat.todochat.models.Developer;
import com.todochat.todochat.services.AuthService;
import com.todochat.todochat.services.DeveloperService;
import com.todochat.todochat.services.TelegramService;

@Component
public class RegisterDeveloperCommand implements BotCommand {

    @Autowired
    private DeveloperService developerService;
    @Autowired
    private AuthService authService;

    @Override
    public void executeCommand(Update update, TaskBotController botController,String[] arguments) {
        // Instanciamos el telegramService
        TelegramService telegramService = new TelegramService(update, botController);
        
        try {
            
        // Obtenemos los argumentos del Developer
        String name = arguments[0];
        String lastname = arguments[1];
        String phone = arguments[2];
        String mail = arguments[3];
        String password = arguments[4];
        String role = arguments[5];

        // Creamos el Developer
        Developer developer = new Developer();
        developer.setName(name);
        developer.setLastname(lastname);
        developer.setPhone(phone);
        developer.setMail(mail);
        developer.setPassword(password);
        developer.setRole(role);

        developerService.createDeveloper(developer);

        // Creamos una autenticacion
        AuthToken authToken = new AuthToken();
        authToken.setChatId(update.getMessage().getChatId().toString());
        authToken.setDeveloper(developer);
        authService.createAuthToken(authToken);
        
        // Opciones a sugerir
        telegramService.addRow(List.of("(VER MIS TAREAS)/listTodo","(AGREGAR TAREA)/addTask"));
        telegramService.addRow("(IR A INICIO)/start");
        telegramService.sendMessage("Haz sido registrado correctamente y te encuentras autenticado");



    } catch (Exception e) {
        telegramService.sendMessage("Error al registrar el Developer"+e.getMessage());
    }

    }
    
}
