package com.todochat.todochat.controllers.botcommands.commands;

import java.util.ArrayList;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import com.todochat.todochat.controllers.TaskBotController;
import com.todochat.todochat.controllers.botcommands.BotCommand;
import com.todochat.todochat.services.AuthService;
import com.todochat.todochat.services.TelegramService;

@Component
public class LoginDeveloperCommand implements BotCommand {

    @Autowired
    private AuthService authService;

    @Override
    public void executeCommand(Update update, TaskBotController botController,String[] arguments) {
        // Obtenemos el correo y la contraseña
        String mail = arguments[0];
        String password = arguments[1];

        // Usamos nuestro servicio de autenticacion para hacer login
        boolean login = authService.loginDeveloper(update, mail, password);

        // Instanciamos el telegramService
        TelegramService telegramService = new TelegramService(update, botController);
        
        

        // Si el login fue exitoso, enviamos un mensaje de bienvenida
        if (login) {
            // TODO: Agregar los botones del teclado que dan a los comandos sugeridos

            // = Agregar un solo elemento a la tabla
            //telegramService.addRow("(Titulo)/comando argumento1 argumento2");  

            // = Agregar varios elementos a la tabla
            List<String> commands = new ArrayList<>();
            commands.add("(Titulo)/comando argumento1 argumento2");
            commands.add("(Titulo)/comando argumento1 argumento2");
            telegramService.addRow(commands);
            

            telegramService.sendMessage("Haz sido autenticado correctamente");

        } else {
            // Si no fue exitoso, enviamos un mensaje de error
            telegramService.sendMessage("Usuario o contraseña no validos");
            // TODO: Agregar los botones del teclado que dan a los comandos sugeridos


        }



    }
    
}
