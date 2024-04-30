package com.todochat.todochat.controllers.botcommands.commands;


import org.telegram.telegrambots.meta.api.objects.Update;

import com.todochat.todochat.controllers.TaskBotController;
import com.todochat.todochat.controllers.botcommands.BotCommand;
import com.todochat.todochat.models.AuthToken;

import com.todochat.todochat.models.Project;
import com.todochat.todochat.services.AuthService;
import com.todochat.todochat.services.ManagerService;
import com.todochat.todochat.services.TelegramService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

 
@Component
public class RemoveDeveloperCommand implements BotCommand {



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
            telegramService.sendMessage("Necesitas autenticacion de manager para eliminar a alguien de un proyecto");
            return;
        }

        try {
            int developerId = Integer.parseInt(arguments[0]);
            Project managerProject = auth.getManager().getProjects().getFirst();

            // Verificamos si el proyecto existe
            if (managerProject == null) {
                telegramService.sendMessage("No hay proyectos asociados a tu cuenta.");
                return;
            }

            

          
            String message = managerService.removeDeveloperFromProject(managerProject.getId(), developerId);
            telegramService.sendMessage(message);
            
        } catch (NumberFormatException e) {
            telegramService.sendMessage("Indice invalido. Por favor agregar un indice.");
        } catch (Exception e) {
            logger.error("Error removing developer from project", e);
            telegramService.sendMessage("Error borrando developer del proyecto.");
        }
    }
}
