package com.todochat.todochat.controllers.botcommands.commands;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import com.todochat.todochat.controllers.TaskBotController;
import com.todochat.todochat.controllers.botcommands.BotCommand;
import com.todochat.todochat.models.AuthToken;
import com.todochat.todochat.models.Task;
import com.todochat.todochat.models.enums.Status;
import com.todochat.todochat.services.AuthService;
import com.todochat.todochat.services.TaskService;
import com.todochat.todochat.services.TelegramService;


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
                    Fecha de terminación: %s

                    Acciones sugeridas:
                    Ver tus tareas: /viewTodo
                    Agregar una tarea: /addTask-nombreTarea-descripcionTarea
                    Cambiar estatus de esta tarea: /changeStatus-%s-estatusNuevo    (estatusNuevo: PENDING | IN_PROGRESS | COMPLETED)
                    """.formatted(task.getId(), task.getName(), task.getDescription(),
                    task.getStatus(), formatData.format(task.getFecha_inicio()), endDate, task.getId());

            // Solo si es desarollador habilitamos la opcion de eliminar la tarea
            if (auth.getDeveloper() != null) {
                telegramService.addRow("(ELIMINAR TAREA) /deleteTodo-"+task.getId());
            }
              
            String progressStatus = "(COLOCAR EN PROGRESO " + task.getName() + ") /changeStatus-" + task.getId()+"-progress";
            String completeStatus = "(COLOCAR COMPLETADO " + task.getName() + ") /changeStatus-" + task.getId()+"-completed";

            List<String> row = new ArrayList<>();
            if(task.getStatus() == Status.PENDING){
                row.add(progressStatus);
            }
            if(task.getStatus() == Status.IN_PROGRESS){
                row.add(completeStatus);
            }

            // Solo si es desarollador habilitamos la opcion de cambiar el estatus
            if (auth.getDeveloper() != null) {
                telegramService.addRow(row);
            }
           
            
            telegramService.addRow("(IR A INICIO) /start");
            telegramService.sendMessage(message);
           
        } catch (Exception e) {
            logger.error("Error al listar tareas", e);
        }
    }
}
