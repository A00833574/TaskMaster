package com.todochat.todochat.controllers.botcommands.commands;

import java.text.SimpleDateFormat;

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
public class ViewTodoCommand implements BotCommand {
    
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

            Task task = taskService.getTaskById(Integer.parseInt(arguments[0]));
            String endDate = "";
            SimpleDateFormat formatData = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

            if (task.getFecha_finalizacion() != null) {
                endDate = formatData.format(task.getFecha_finalizacion());
            }
            else {
                endDate = "Esta tarea no ha sido terminada";
            }

            String message = """
                    Detalles de tarea
                    
                    ID de tarea: %s
                    Nombre: %s
                    Descripción: %s
                    Status: %s

                    Fecha de creación: %s
                    Fecha de terminación: $s

                    Acciones sugeridas:
                    Ver tus tareas: /viewTodo
                    Agregar una tarea: /addTodo-nombreTarea-descripcionTarea
                    Cambiar estatus de esta tarea: /changeTodoStatus-$s-estatusNuevo    (estatusNuevo: PENDING | IN_PROGRESS | COMPLETED)
                    """.formatted(task.getId(), task.getName() + task.getDescription(),
                    task.getStatus(), formatData.format(task.getFecha_inicio()), endDate, task.getId());

            telegramService.sendMessage(message);
           
        } catch (Exception e) {
            logger.error("Error al listar tareas", e);
        }
    }
}
