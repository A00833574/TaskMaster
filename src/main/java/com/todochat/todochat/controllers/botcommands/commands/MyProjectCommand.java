package com.todochat.todochat.controllers.botcommands.commands;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import com.todochat.todochat.controllers.TaskBotController;
import com.todochat.todochat.controllers.botcommands.BotCommand;
import com.todochat.todochat.models.AuthToken;
import com.todochat.todochat.models.Proyect;
import com.todochat.todochat.services.AuthService;
import com.todochat.todochat.services.TelegramService;

@Component
public class MyProjectCommand implements BotCommand{

    @Autowired
    private AuthService authService;

    @Override
    public void executeCommand(Update update, TaskBotController botController, String[] arguments){
        // Instanciamos nuestro servicio de telegram
        TelegramService telegramService = new TelegramService(update,botController);

        // Autenticamos al usuario
        AuthToken auth = authService.authenticate(update);

        // Verificamos si el usuario esta autenticado
        if(auth == null){
            telegramService.sendMessage("No te encuentras autenticado, por favor autentícate con el comando /loginDev o /loginManager, por ejemplo /loginDev-correo-contraseña");
            return;
        }

        // Verificamos si es un manager
        if(auth.getManager() == null){
            telegramService.sendMessage("Debes autenticarte como manager para poder ver tu proyecto");
            return;
        }

        // Obtenemos el proyecto del manager
        List<Proyect> proyects = auth.getManager().getProyects();
        Proyect proyect = proyects.get(0);

        String message = """
                Tu proyecto
                Id: %s
                Nombre: %s
               

                """.formatted(proyect.getId(),proyect.getName());

        telegramService.sendMessage(message);
    }
}
