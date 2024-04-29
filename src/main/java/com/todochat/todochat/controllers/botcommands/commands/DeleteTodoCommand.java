package com.todochat.todochat.controllers.botcommands.commands;

import org.telegram.telegrambots.meta.api.objects.Update;

import com.todochat.todochat.controllers.TaskBotController;
import com.todochat.todochat.controllers.botcommands.BotCommand;
import com.todochat.todochat.models.AuthToken;
import com.todochat.todochat.models.Task;
import com.todochat.todochat.services.AuthService;
import com.todochat.todochat.services.TaskService;
import com.todochat.todochat.services.TelegramService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class DeleteTodoCommand implements BotCommand {
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
            telegramService.sendMessage("No estas autenticado, por favor usa el comando /login para autenticarte");
            return;
        }
        // Verificamos si la autenticacion es de desarrollador
        if (auth.getDeveloper() == null) {
            telegramService.sendMessage("Necesitas autenticacion de desarrollador para agregar tareas");
            return;
        }

        try {
            // Verificamos si hay argumentos
            if (arguments.length == 0) {
                telegramService.sendMessage("Por favor ingresa el id de la tarea que deseas eliminar");
                return;
            }
            // Obtenemos el id de la tarea
            int id = Integer.parseInt(arguments[0]);
            // Obtenemos la tarea
            Task task = taskService.getTaskById(id);
            // Verificamos si la tarea existe
            if (task == null) {
                telegramService.sendMessage("La tarea no existe");
                return;
            }
            // Verificamos si la tarea es del usuario autenticado
            if (task.getDeveloper().getId() != auth.getDeveloper().getId()) {
                telegramService.sendMessage("No puedes eliminar tareas de otros desarrolladores");
                return;
            }

            // Eliminamos la tarea
            taskService.deleteTaskById(id);
            telegramService.sendMessage("Tarea eliminada");
        } catch (Exception e) {
            logger.error("Error al eliminar tarea",e);
            telegramService.sendMessage("Error al eliminar tarea");
        }
    }
}
