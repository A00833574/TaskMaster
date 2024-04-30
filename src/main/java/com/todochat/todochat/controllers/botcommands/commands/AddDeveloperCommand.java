package com.todochat.todochat.controllers.botcommands.commands;

import java.util.List;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import com.todochat.todochat.controllers.TaskBotController;
import com.todochat.todochat.controllers.botcommands.BotCommand;
import com.todochat.todochat.models.AuthToken;
import com.todochat.todochat.models.Developer;
import com.todochat.todochat.models.Proyect;
import com.todochat.todochat.services.AuthService;
import com.todochat.todochat.services.ManagerService;
import com.todochat.todochat.services.ProyectService;
import com.todochat.todochat.services.DeveloperService;
import com.todochat.todochat.services.TelegramService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

 
@Component
public class AddDeveloperCommand implements BotCommand {
    
    @Autowired
    private DeveloperService developerService;

    @Autowired
    private ProyectService proyectService;

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
            telegramService.sendMessage("Necesitas autenticacion de manager para asignar desarrolladores a proyectos.");
            return;
        }

        try {
            int developerIndex = Integer.parseInt(arguments[0]) - 1;

            List<Developer> developers = developerService.getAllDevelopers();
            Developer developer = developers.get(developerIndex);

            Proyect managerProyect = proyectService.getProyectByManagerId(auth.getManager().getId());
            if (managerProyect == null) {
                telegramService.sendMessage("No se encontró un proyecto asociado a tu cuenta de manager.");
            return;
            }

            managerService.assignProyect(managerProyect.getId(), developer.getId());
            String successMessage = String.format("Desarrollador %s %s ha sido agregado exitosamente al proyecto %s.", developer.getName(), developer.getLastname(), managerProyect.getName());
            telegramService.sendMessage(successMessage);
        }
        catch (NumberFormatException | IndexOutOfBoundsException e) {
            telegramService.sendMessage("Índice de desarrollador inválido. Por favor, inténtalo de nuevo.");

        } catch (Exception e) {
            logger.error("Error while adding developer to project", e);
            telegramService.sendMessage("Error al agregar el desarrollador al proyecto.");
        }
    }
}
