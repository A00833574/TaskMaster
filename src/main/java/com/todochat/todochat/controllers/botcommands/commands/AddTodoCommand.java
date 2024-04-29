package com.todochat.todochat.controllers.botcommands.commands;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;


import com.todochat.todochat.controllers.TaskBotController;
import com.todochat.todochat.controllers.botcommands.BotCommand;
import com.todochat.todochat.models.AuthToken;
import com.todochat.todochat.models.Task;
import com.todochat.todochat.models.enums.Status;
import com.todochat.todochat.services.AuthService;
import com.todochat.todochat.services.TaskService;
import com.todochat.todochat.services.TelegramService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

 
@Component
public class AddTodoCommand implements BotCommand {
    
    @Autowired
    private TaskService taskService;

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
            telegramService.sendMessage("No estas autenticado, por favor usa el comando /loginDev o /loginManager para autenticarte");
            return;
        }
        // Verificamos si la autenticacion es de desarrollador
        if (auth.getDeveloper() == null) {
            telegramService.sendMessage("Necesitas autenticacion de desarrollador para agregar tareas");
            return;
        }

        try {
            Task newItem = new Task();
            Date currentDate = new Date();
            newItem.setName(arguments[0]);
            newItem.setDescription(arguments[1]);
            newItem.setFecha_inicio(currentDate);
            newItem.setStatus(Status.PENDING);
            newItem.setDeveloper(auth.getDeveloper());
            taskService.createTask(newItem);

            List<String> commands = new ArrayList<>();
            commands.add("Regresar a página principal");
            commands.add("Ver detalles de la tarea");
            commands.add("Cambiar estatus de la tarea");
            commands.add("Eliminar la tarea");

            telegramService.sendMessage("Tarea agregada correctamente");
        } catch (Exception e) {
            logger.error("Error al agregar una tarea", e);
        }
    }
}
