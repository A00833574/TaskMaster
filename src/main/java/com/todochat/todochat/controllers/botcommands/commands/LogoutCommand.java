package com.todochat.todochat.controllers.botcommands.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import com.todochat.todochat.controllers.TaskBotController;
import com.todochat.todochat.controllers.botcommands.BotCommand;
import com.todochat.todochat.services.AuthService;
import com.todochat.todochat.services.TelegramService;

@Component
public class LogoutCommand implements BotCommand{

    @Autowired
    private AuthService authService;

    @Override
    public void executeCommand(Update update, TaskBotController botController, String[] arguments){
        // Instanciamos nuestro telegram service
        TelegramService telegramService = new TelegramService(update,botController);

        // Ejecutamos el metodo de logout
        authService.logout(update);

        telegramService.clearRow();
        
        // Enviamos un mensaje de despedida
        telegramService.sendMessage("Haz cerrado sesion correctamente \n Puede iniciar sesion nuevamente con el comando /loginDev o /loginManager, por ejemplo /loginDev-correo-contraseña");

    }
}
