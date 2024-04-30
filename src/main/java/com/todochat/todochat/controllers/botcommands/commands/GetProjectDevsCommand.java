package com.todochat.todochat.controllers.botcommands.commands;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import com.todochat.todochat.controllers.TaskBotController;
import com.todochat.todochat.controllers.botcommands.BotCommand;
import com.todochat.todochat.models.AuthToken;
import com.todochat.todochat.models.Developer;
import com.todochat.todochat.models.Manager;
import com.todochat.todochat.models.Project;
import com.todochat.todochat.services.AuthService;
import com.todochat.todochat.services.TelegramService;

@Component
public class GetProjectDevsCommand implements BotCommand {

    @Autowired
    private AuthService authService;


    @Override
    public void executeCommand(Update update, TaskBotController botController, String[] arguments) {
        // Generamos el servicio de telegram
        TelegramService telegramService = new TelegramService(update, botController);

        // Verificamos si esta autenticado
        AuthToken auth = authService.authenticate(update);

        if (auth == null) {
            telegramService.sendMessage("Projecto no encontrado");
            return;
        }

        // Verificamos si la autenticacion es de manager
        if (auth.getManager() == null) {
            telegramService.sendMessage("Necesitas autenticacion de manager para ver los desarrolladores de un proyecto");
            return;
        }

        // Obtenemos el manager
        Manager manager = auth.getManager();

        // Obtenemos los proyectos del manager
        List<Project> projects = manager.getProjects();

        if (projects.isEmpty()) {
            telegramService.sendMessage("No cuentas con un proyecto, gra uno con el comando /addProject-<Nombre de Proyecto>");
            return;
        }
        Project project = manager.getProjects().get(0);

        

        if (project == null) {
            telegramService.sendMessage("Proyecto no encontrado");
            return;
        }

        List<Developer> developers =  project.getDevelopers();
       

        StringBuilder message = new StringBuilder();
            message.append("Desarrolladores disponibles: \n");
            for (Developer developer : developers) {
                message.append("ID: ").append(developer.getId()).append("\n");
                message.append("Nombre: ").append(developer.getName()).append("\n");
                message.append("Apellido: ").append(developer.getLastname()).append("\n");
                message.append("Correo: ").append(developer.getMail()).append("\n");
                message.append("Telefono: ").append(developer.getPhone()).append("\n");
                message.append("\n");
            }

            for (Developer developer : developers) {
                telegramService.addRow(List.of("(TAREAS DE " + developer.getName() + ")/getDevTasks-" + developer.getId(),"(ELIMINAR A " + developer.getName() + ")/removeDeveloper-" + developer.getId()));
                
            }
            telegramService.addRow("(IR A INICIO)/start");
            telegramService.sendMessage(message.toString());


    }

}
