package com.todochat.todochat.controllers.botcommands.commands;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import com.todochat.todochat.controllers.TaskBotController;
import com.todochat.todochat.controllers.botcommands.BotCommand;
import com.todochat.todochat.models.AuthToken;
import com.todochat.todochat.models.Developer;
import com.todochat.todochat.models.Task;
import com.todochat.todochat.models.enums.Status;
import com.todochat.todochat.services.AuthService;
import com.todochat.todochat.services.TaskService;
import com.todochat.todochat.services.TelegramService;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

 
@Component
public class changeStatusCommand implements BotCommand {
    
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

        // Si no se pasaran los parametros necesarios se le informa al usuario
        if (arguments.length < 2) {
            telegramService.sendMessage("No se pasaron los parametros necesarios para cambiar el status de la tarea. Recuerda usar la notación /changeStatus-<idTarea>-<estatusNuevo (estatusNuevo: pending | progress | completed)>");
            return;
        }

        Developer developer = auth.getDeveloper();

        try {

            Task task = taskService.getTaskById(Integer.parseInt(arguments[0]));
            String endDate = "";
            String msgRespuesta = "";
            SimpleDateFormat formatData = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            
            if (arguments.length == 2) {
                switch (arguments[1]){
                    case "pending":
                        if (task.getStatus() != Status.PENDING) { 
                            task.setStatus(Status.PENDING);
                            taskService.updateTask(task);
                            msgRespuesta = "El status de la tarea ha sido actualizada correctamente. Estos son los detalles nuevos de la tarea:";
                        }
                        else {
                            msgRespuesta = "El status que se deseó asignar ya estaba establecido en la tarea. Estos son los detalles de la tarea:";
                        }
                        break;
                    case "progress":
                        if (task.getStatus() != Status.IN_PROGRESS) { 
                            task.setStatus(Status.IN_PROGRESS);
                            taskService.updateTask(task); 
                            msgRespuesta = "El status de la tarea ha sido actualizada correctamente. Estos son los detalles nuevos de la tarea:";
                        }
                        else {
                            msgRespuesta = "El status que se deseó asignar ya estaba establecido en la tarea. Estos son los detalles de la tarea:";
                        }
                        break;
                    case "completed":
                        if (task.getStatus() != Status.COMPLETED) { 
                            task.setStatus(Status.COMPLETED);
                            task.setFecha_finalizacion(new Date());
                            taskService.updateTask(task);
                            msgRespuesta = "El status de la tarea ha sido actualizada correctamente. Estos son los detalles nuevos de la tarea:";
                        }
                        else {
                            msgRespuesta = "El status que se deseó asignar ya estaba establecido en la tarea. Estos son los detalles de la tarea:";
                        }
                        break;
                    default:
                        msgRespuesta = "El valor insertado para la nueva asignación de status no es válida. Recuerda utilizar PENDING, IN_PROGRESS, o COMPLETED como valores para el status. Estos son los detalles de la tarea:";
                }
                
                // Se obtiene por string la fecha de finalización almacenada
                if (task.getFecha_finalizacion() != null) {
                    endDate = formatData.format(task.getFecha_finalizacion());
                }
                else {
                    endDate = "Esta tarea no ha sido terminada";
                }

                String message = """
                        %s
                        
                        ID de tarea: %s
                        Nombre: %s
                        Descripción: %s
                        Status: %s

                        Fecha de creación: %s
                        Fecha de terminación: %s

                        Acciones sugeridas:
                        Ver tus tareas: /viewTodo
                        Agregar una tarea: /addTodo-nombreTarea-descripcionTarea
                        Cambiar estatus de esta tarea: /changeStatus-%s-estatusNuevo    (estatusNuevo: PENDING | IN_PROGRESS | COMPLETED)
                        """.formatted(msgRespuesta, task.getId(), task.getName(), task.getDescription(),
                        task.getStatus(), formatData.format(task.getFecha_inicio()), endDate, task.getId());
                
                telegramService.addRow("(VER TAREAS) /listTodo");
                telegramService.addRow("(IR A INICIO) /start");
                telegramService.sendMessage(message);
            }
            else {
                telegramService.addRow("(VER TAREAS) /listTodo");
                telegramService.addRow("(IR A INICIO) /start");
                telegramService.sendMessage("Se intentó utilizar /changeStatus sin los argumentos necesarios. Recuerda usar la notación /changeStatus-idTarea-estatusNuevo (estatusNuevo: PENDING | IN_PROGRESS | COMPLETED).");
            }
           
        } catch (Exception e) {
            logger.error("Error al listar tareas", e);
        }
    }
}
