package com.todochat.todochat.controllers.botcommands.commands;

import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import com.todochat.todochat.controllers.TaskBotController;
import com.todochat.todochat.controllers.botcommands.BotCommand;
import com.todochat.todochat.models.AuthToken;
import com.todochat.todochat.models.Developer;
import com.todochat.todochat.services.AuthService;
import com.todochat.todochat.services.ManagerService;
import com.todochat.todochat.services.TelegramService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class UnassignedDevelopersCommand implements BotCommand {
    @Autowired
    private ManagerService managerService;

    @Autowired
    private AuthService authService;

	private static final Logger logger = LoggerFactory.getLogger(TaskBotController.class);

    @Override
    public void executeCommand(Update update, TaskBotController botController,String[] arguments) {
        // Generamos el servicio de telegram
        TelegramService telegramService = new TelegramService(update,botController);

        // Verificamos si esta autenticado
        AuthToken auth = authService.authenticate(update);

        // Verificamos si hay autenticacion
        if (auth == null) {
            telegramService.sendMessage("No estas autenticado, por favor usa el comando /login para autenticarte");
            return;
        }
        // Verificamos si la autenticacion es de desarrollador
        if (auth.getManager() == null) {
            telegramService.sendMessage("Necesitas autenticacion de desarrollador para agregar tareas");
            return;
        }

        try {
            // Obtenemos la tarea
            List<Developer> unassignedDevelopers = managerService.getDevelopersByProjectId(null);
            // Verificamos si la tarea existe
            if (unassignedDevelopers.size() == 0) {
                telegramService.sendMessage("Por el momento no hay desarrolladores disponibles.");
                return;
            }
            
            StringBuilder message = new StringBuilder();
            message.append("Desarrolladores disponibles: \n");
            for (Developer developer : unassignedDevelopers) {
                message.append("ID: ").append(developer.getId()).append("\n");
                message.append("Nombre: ").append(developer.getName()).append("\n");
                message.append("Apellido: ").append(developer.getLastname()).append("\n");
                message.append("Correo: ").append(developer.getMail()).append("\n");
                message.append("Telefono: ").append(developer.getPhone()).append("\n");
                message.append("\n");
            }

            for (Developer developer : unassignedDevelopers) {
                telegramService.addRow("(ASIGNAR A " + developer.getName() + ") /addDeveloper-" + developer.getId());
            }
            telegramService.addRow("(IR A INICIO)/start");
            telegramService.sendMessage(message.toString());
        } catch (Exception e) {
            logger.error("Error al eliminar tarea",e);
            telegramService.sendMessage("Error al eliminar tarea");
        }
    }
}
