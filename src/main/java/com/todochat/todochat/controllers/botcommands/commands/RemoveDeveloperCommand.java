package com.todochat.todochat.controllers.botcommands.commands;

import java.util.List;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import com.todochat.todochat.controllers.TaskBotController;
import com.todochat.todochat.controllers.botcommands.BotCommand;
import com.todochat.todochat.models.AuthToken;
import com.todochat.todochat.models.Developer;
import com.todochat.todochat.models.Project;
import com.todochat.todochat.services.AuthService;
import com.todochat.todochat.services.ManagerService;
import com.todochat.todochat.services.ProjectService;
import com.todochat.todochat.services.TelegramService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

 
@Component
public class RemoveDeveloperCommand implements BotCommand {

    @Autowired
    private ProjectService projectService;

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
        if (auth.getDeveloper() == null) {
            telegramService.sendMessage("Necesitas autenticacion de desarrollador para agregar projectos");
            return;
        }

        try {
            int developerIndex = Integer.parseInt(arguments[0]) - 1;
            Project managerProject = projectService.getProjectByManagerId(auth.getManager().getId());

            if (managerProject == null) {
                telegramService.sendMessage("No hay projectos asociados a tu cuenta.");
                return;
            }

            List<Developer> developers = managerProject.getDevelopers();
            if (developerIndex < 0 || developerIndex >= developers.size()) {
                telegramService.sendMessage("Indice invalido.");
                return;
            }

            Developer developer = developers.get(developerIndex);
            String message = managerService.removeDeveloperFromProject(managerProject.getId(), developer.getId());
            telegramService.sendMessage(message);
            
        } catch (NumberFormatException e) {
            telegramService.sendMessage("Indice invalido. Por favor agregar un indice.");
        } catch (Exception e) {
            logger.error("Error removing developer from project", e);
            telegramService.sendMessage("Error borrando developer del projecto.");
        }
    }
}
