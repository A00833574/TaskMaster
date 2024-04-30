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
import com.todochat.todochat.models.Project;
import com.todochat.todochat.models.Task;
import com.todochat.todochat.models.enums.Status;
import com.todochat.todochat.services.AuthService;
import com.todochat.todochat.services.TaskService;
import com.todochat.todochat.services.TelegramService;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

 
@Component
public class ListProjectTasksCommand implements BotCommand {
    
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
        if (auth.getManager() == null) {
            telegramService.sendMessage("Necesitas autenticacion de desarrollador para agregar tareas");
            return;
        }

        Manager manager = auth.getManager();

        // Obtenemos el projecto del manager
        Project project = manager.getProjects().get(0);

        try {

            List<Task> tasksList = taskService.getAllTasksByProject(project.getId());
            String tasksMsg = "";
            
            if (tasksList.isEmpty()){
                telegramService.sendMessage("El projecto no cuenta con ninguna tarea.");
            }
            else {

                // Se agregan las tareas enlistadas al teclado
                for (Task task : tasksList) {
                    String details = "(DETALLES DE " + task.getName() + ") /viewTodo-" + task.getId();
                  

                    telegramService.addRow(details);
                }

                telegramService.addRow("(IR A INICIO)/start");

                // Se construye el mensake de texto que regresa el chatbot
                for (Task task : tasksList) {
                    tasksMsg = tasksMsg +task.getId() + "-" + task.getName() + " --- " + task.getStatus() + "\n";
                }
                telegramService.sendMessage("Tareas asignadas del projecto " + project.getName() + ":\n\n" + tasksMsg + "\nEscribe '/viewTodo-id de tarea' para desplegar los detalles de una tarea.");

            }
           
        } catch (Exception e) {
            logger.error("Error al listar tareas", e);
        }
    }
}
