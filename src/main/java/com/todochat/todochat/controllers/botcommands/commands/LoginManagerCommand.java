package com.todochat.todochat.controllers.botcommands.commands;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import com.todochat.todochat.controllers.TaskBotController;
import com.todochat.todochat.controllers.botcommands.BotCommand;
import com.todochat.todochat.models.Manager;
import com.todochat.todochat.repositories.ManagerRepository;
import com.todochat.todochat.services.AuthService;
import com.todochat.todochat.services.TelegramService;

@Component
public class LoginManagerCommand implements BotCommand {

    @Autowired
    private AuthService authService;

    @Autowired
    private ManagerRepository managerRepository;

    @Override
    public void executeCommand(Update update, TaskBotController botController, String[] arguments) {
        // Obtenemos el correo y la contraseña
        String mail = arguments[0];
        String password = arguments[1];

        // Usamos nuestro servicio de autenticacion para hacer login
        boolean login = authService.loginManager(update, mail, password);

        // Instanciamos el telegramService
        TelegramService telegramService = new TelegramService(update, botController);

        // Obtenemos el manager
        Manager manager = managerRepository.findByMail(mail);

        // Si el login fue exitoso, enviamos un mensaje de bienvenida
        if (login) {
            List<String> firstRow = new ArrayList<>();
            if(manager.getProjects().size() == 0){
                firstRow.add("(CREAR PROYECTO) /addProject");
            }else{
                firstRow.add("(VER MI PROYECTO) /myProject");
                firstRow.add("(VER TAREAS DE PROYECTO) /projectTasks");
            }

            telegramService.addRow(firstRow);
            telegramService
                    .addRow(List.of("(VER DESARROLLADORES) /getProjectDevs","(VER DESAROLLADORES PENDIENTES)/unassignedDevs" ));
            telegramService.addRow("(IR A INICIO) /start");

            String message = """
                    Version 1.0.1
                    Bienvenido manager %s
                    Tus datos:
                    Nombre completo %s
                    Correo: %s
                    Telefono: %s
                    Rol: %s

                    ¿Que deseas hacer?
                    Ver tus tareas: /listTodo
                    Agregar una tarea: /addTask-nombreTarea-descripcionTarea
                    """.formatted(manager.getName(), manager.getName() + " " + manager.getLastname(), manager.getMail(),
                    manager.getPhone(), manager.getRole());
            telegramService.sendMessage(message);

        } else {
            // Si no fue exitoso, enviamos un mensaje de error
            telegramService.sendMessage("Usuario o contraseña no validos");
        }

    }

}
