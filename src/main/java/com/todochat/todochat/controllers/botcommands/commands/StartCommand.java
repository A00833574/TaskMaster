package com.todochat.todochat.controllers.botcommands.commands;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.todochat.todochat.controllers.TaskBotController;
import com.todochat.todochat.controllers.botcommands.BotCommand;
import com.todochat.todochat.models.AuthToken;
import com.todochat.todochat.services.AuthService;
import com.todochat.todochat.services.TelegramService;
import com.todochat.todochat.utils.BotLabels;
 
@Component
public class StartCommand implements BotCommand {

    @Autowired
    private AuthService authService;

    @Override
    public void executeCommand(Update update, TaskBotController botController,String[] arguments) {
        
        // Instanciamos nuestro servicio de telegram
        TelegramService telegramService = new TelegramService(update,botController);

        // Verificamos si el usuario esta autenticado
        AuthToken auth = authService.authenticate(update);

        // Si no esta autenticado le decimos que se tiene que autenticar
        if (auth == null) {
            telegramService.sendMessage("No te encuentras autenticado, por favor autentícate con el comando /loginDev o /loginManager, por ejemplo /loginDev-correo-contraseña");
            return;
        }

       

        // Si hay autenticacion verificamos si se trata de un desarollador
        if(auth.getDeveloper() != null){
            telegramService.addRow(List.of("(VER MIS TAREAS)/myTasks","(AGREGAR TAREA)/addTask"));
            telegramService.addRow(List.of("(MODIFICAR MIS DATOS)/profile","(CERRAR SESION)/logout"));
            telegramService.sendMessage("Bienvenido desarrollador "+auth.getDeveloper().getName()+"\n ¿Que deseas hacer?" + "\n Ver tus tareas: /myTasks \n Agregar una tarea: /addTask-nombreTarea-descripcionTarea " );
            return;
        }


    }
}
