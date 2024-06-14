package com.todochat.todochat.controllers.botcommands.commands;



import org.telegram.telegrambots.meta.api.objects.Update;


import com.todochat.todochat.controllers.TaskBotController;
import com.todochat.todochat.controllers.botcommands.BotCommand;
import com.todochat.todochat.models.AuthToken;
import com.todochat.todochat.models.Project;
import com.todochat.todochat.services.AuthService;
import com.todochat.todochat.services.ProjectService;
import com.todochat.todochat.services.TelegramService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

 
@Component
public class AddProjectCommand implements BotCommand {
    
    @Autowired
    private ProjectService proyectService;

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
            telegramService.sendMessage("Necesitas autenticacion de desarrollador para agregar proyectos");
            return;
        }

        // Verificamos si el manager ya tiene un proyecto
        if (auth.getManager().getProjects().size() > 0) {
            telegramService.sendMessage("Ya tienes un proyecto creado, no puedes tener mas de un proyecto");
            return;
        }

        try {
            Project newItem = new Project();
            newItem.setName(arguments[0]);
            newItem.setManager(auth.getManager());
            proyectService.createProject(newItem);

            // TODO: Agregar opciones de teclado

            telegramService.sendMessage("Projecto agregado correctamente, puedes agregar tareas con /addTask, agregar developers con /addDeveloper y ver el estado del proyecto con /showProject");

           
        } catch (Exception e) {
            logger.error("Error al agregar el proyecto", e);
        }
    }
}
