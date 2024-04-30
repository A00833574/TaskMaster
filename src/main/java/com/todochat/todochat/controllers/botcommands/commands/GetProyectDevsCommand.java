package com.todochat.todochat.controllers.botcommands.commands;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import com.todochat.todochat.controllers.TaskBotController;
import com.todochat.todochat.controllers.botcommands.BotCommand;
import com.todochat.todochat.models.AuthToken;
import com.todochat.todochat.models.Proyect;
import com.todochat.todochat.services.AuthService;
import com.todochat.todochat.services.ProyectService;
import com.todochat.todochat.services.TelegramService;

@Component
public class GetProyectDevsCommand implements BotCommand {

    @Autowired
    private AuthService authService;

    @Autowired
    private ProyectService proyectService;

    @Override
    public void executeCommand(Update update, TaskBotController botController, String[] arguments) {
        // Generamos el servicio de telegram
        TelegramService telegramService = new TelegramService(update, botController);

        // Verificamos si esta autenticado
        AuthToken auth = authService.authenticate(update);

        if (auth == null) {
            telegramService.sendMessage("Proyecto no encontrado");
            return;
        }

        // Verificamos si la autenticacion es de manager
        if (authService.authenticate(update).getManager() == null) {
            telegramService.sendMessage("Necesitas autenticacion de manager para ver los desarrolladores de un proyecto");
            return;
        }

        // Verificamos si el comando tiene argumentos
        if (arguments.length == 0) {
            telegramService.sendMessage("/getProjectDevs [id del proyecto]");
            return;
        }

        // Obtenemos el id del proyecto
        int proyectId = Integer.parseInt(arguments[0]);

        // Obtenemos el proyecto
        Proyect proyect = proyectService.getProyectById(proyectId);

        if (proyect == null) {
            telegramService.sendMessage("Proyecto no encontrado");
            return;
        }

        List<String> developers = new ArrayList<>();
        proyect.getDevelopers().forEach(developer -> developers.add(developer.getName()));

        List<String> commands = new ArrayList<>();
        commands.add("(Obtener tareas de un desarrollador)/getDevTasks [id del desarrollador]");

        telegramService.sendMessage("Desarrolladores del proyecto /n" + developers.toString().replace("[", "").replace("]", "").replace(",", "\n"));



    }

}
