package com.todochat.todochat.controllers.botcommands.commands;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import com.todochat.todochat.controllers.TaskBotController;
import com.todochat.todochat.controllers.botcommands.BotCommand;
import com.todochat.todochat.models.AuthToken;
import com.todochat.todochat.models.Developer;
import com.todochat.todochat.models.Task;
import com.todochat.todochat.services.AuthService;
import com.todochat.todochat.services.DeveloperService;
import com.todochat.todochat.services.TelegramService;

@Component
public class GetDevTasksCommand implements BotCommand {

    @Autowired
    private AuthService authService;

    @Autowired
    private DeveloperService developerService;

    @Override
    public void executeCommand(Update update, TaskBotController botController, String[] arguments) {
        // Generamos el servicio de telegram
        TelegramService telegramService = new TelegramService(update, botController);

        // Verificamos si esta autenticado
        AuthToken auth = authService.authenticate(update);

        if (auth == null) {
            telegramService.sendMessage("Usuario no encontrado");
            return;
        }

        // Verificamos si la autenticacion es de manager
        if (auth.getManager() == null) {
            telegramService.sendMessage("Necesitas autenticacion de manager para ver las tareas de un desarrollador");
            return;
        }

        // Verificamos si el comando tiene argumentos
        if (arguments.length == 0) {
            telegramService.sendMessage("/getDevTasks-[id del desarrollador]");
            return;
        }

        // Obtenemos el id del desarrollador
        int developerId = Integer.parseInt(arguments[0]);

        // Obtenemos el desarrollador
        Developer developer = developerService.getDeveloperById(developerId);

        if (developer == null) {
            telegramService.sendMessage("Desarrollador no encontrado");
            return;
        }

        // Obtenemos las tareas del desarrollador
        List<Task> tasks = developer.getTareas();

        if (tasks.isEmpty()) {
            telegramService.sendMessage("Este desarrollador no tiene tareas asignadas");
            return;
        }

        // Se agregan las tareas enlistadas al teclado
        for (Task task : tasks) {
            String details = "(DETALLES DE " + task.getName() + ") /viewTodo-" + task.getId();
            

            telegramService.addRow(details);
        }

        telegramService.addRow("(IR A INICIO)/start");
        String tasksMsg = "";
        // Se construye el mensake de texto que regresa el chatbot
        for (Task task : tasks) {
            tasksMsg = tasksMsg + task.getId() + "-" + task.getName() + " --- " + task.getStatus() + "\n";
        }
        telegramService.sendMessage("Tareas asignadas a " + developer.getName() + ":\n\n" + tasksMsg
                + "\nEscribe '/viewTodo-id de tarea' para desplegar los detalles de una tarea.");
    }
}
