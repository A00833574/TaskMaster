package com.todochat.todochat.controllers.botcommands.commands;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import com.todochat.todochat.controllers.TaskBotController;
import com.todochat.todochat.controllers.botcommands.BotCommand;
import com.todochat.todochat.models.Developer;
import com.todochat.todochat.repositories.DeveloperRepository;
import com.todochat.todochat.services.AuthService;
import com.todochat.todochat.services.TelegramService;

@Component
public class LoginDeveloperCommand implements BotCommand {

    @Autowired
    private AuthService authService;

    @Autowired
    private DeveloperRepository developerRepository;

    @Override
    public void executeCommand(Update update, TaskBotController botController,String[] arguments) {
        // Obtenemos el correo y la contraseña
        String mail = arguments[0];
        String password = arguments[1];

        // Usamos nuestro servicio de autenticacion para hacer login
        boolean login = authService.loginDeveloper(update, mail, password);

        // Instanciamos el telegramService
        TelegramService telegramService = new TelegramService(update, botController);
        
        // Obtenemos el desarrollador
        Developer developer = developerRepository.findByMail(mail);

        // Si el login fue exitoso, enviamos un mensaje de bienvenida
        if (login) {
            telegramService.addRow(List.of("(VER MIS TAREAS)/listTodo", "(AGREGAR TAREA)/addTask"));
            telegramService.addRow("(CERRAR SESION)/logout");
            String message = """
                    Version 1.0.1
                    Bienvenido desarrollador %s
                    Tus datos:
                    Nombre completo %s
                    Correo: %s
                    Telefono: %s
                    Rol: %s

                    ¿Que deseas hacer?
                    Ver tus tareas: /listTodo
                    Agregar una tarea: /addTask-nombreTarea-descripcionTarea
                    """.formatted(developer.getName(), developer.getName() + " " + developer.getLastname(),
                    developer.getMail(), developer.getPhone(), developer.getRole());
            telegramService.sendMessage(message);

        } else {
            // Si no fue exitoso, enviamos un mensaje de error
            telegramService.sendMessage("Usuario o contraseña no validos");
           


        }



    }
    
}
