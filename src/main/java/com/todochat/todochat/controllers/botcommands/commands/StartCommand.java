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
import com.todochat.todochat.models.Manager;
import com.todochat.todochat.services.AuthService;
import com.todochat.todochat.services.TelegramService;


@Component
public class StartCommand implements BotCommand {

    @Autowired
    private AuthService authService;

    @Override
    public void executeCommand(Update update, TaskBotController botController, String[] arguments) {

        // Instanciamos nuestro servicio de telegram
        TelegramService telegramService = new TelegramService(update, botController);

        // Verificamos si el usuario esta autenticado
        AuthToken auth = authService.authenticate(update);

        // Si no esta autenticado le decimos que se tiene que autenticar
        if (auth == null) {
            telegramService.sendMessage(
                    "No te encuentras autenticado, por favor autentícate con el comando /loginDev o /loginManager, por ejemplo /loginDev-correo-contraseña");
            return;
        }

        // Si hay autenticacion verificamos si se trata de un desarollador
        if (auth.getDeveloper() != null) {

            Developer developer = auth.getDeveloper();

            telegramService.addRow(List.of("(VER MIS TAREAS)/listTodo", "(AGREGAR TAREA)/addTask"));
            telegramService.addRow("(CERRAR SESION)/logout");
            String message = """
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
            return;
        }

        // Si se trata de un manager
        if (auth.getManager() != null) {

            Manager manager = auth.getManager();


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
            telegramService.addRow("(CERRAR SESION) /logout");

            String message = """
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
            return;
        }

    }
}
