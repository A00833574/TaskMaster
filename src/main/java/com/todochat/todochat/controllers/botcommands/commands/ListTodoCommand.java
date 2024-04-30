package com.todochat.todochat.controllers.botcommands.commands;

import java.util.List;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import com.todochat.todochat.controllers.TaskBotController;
import com.todochat.todochat.controllers.botcommands.BotCommand;
import com.todochat.todochat.models.AuthToken;
import com.todochat.todochat.models.Developer;
import com.todochat.todochat.models.Task;
import com.todochat.todochat.services.AuthService;
import com.todochat.todochat.services.TaskService;
import com.todochat.todochat.services.TelegramService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

 
@Component
public class ListTodoCommand implements BotCommand {
    
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

        Developer developer = auth.getDeveloper();

        try {

            List<Task> tasksList = taskService.getAllTasksByDeveloper(developer.getId());
            String tasksMsg = "";
            
            if (tasksList.isEmpty()){
                telegramService.sendMessage("No cuentas con ninguna tarea asignada. Para agregar una tarea, utiliza el comando '/addTodo-nombre de tarea-descripcion de tarea' respetando los guiones.");
            }
            else {

                // Se construye el mensake de texto que regresa el chatbot
                for (Task task : tasksList) {
                    tasksMsg = tasksMsg + task.getName() + " --- " + task.getStatus() + "\n";
                }
                telegramService.sendMessage("Tareas asignadas a " + developer.getName() + ":\n\n" + tasksMsg + "\nEscribe '/viewTodo-nombre de tarea' para desplegar los detalles de una tarea.");

                // Se agregan las tareas enlistadas al teclado
                for (Task task : tasksList) {
                    telegramService.addRow("(*DETALLES DE " + task.getName() + "*) /viewTodo-" + task.getId());
                }

            }
           
        } catch (Exception e) {
            logger.error("Error al listar tareas", e);
        }
    }
}
