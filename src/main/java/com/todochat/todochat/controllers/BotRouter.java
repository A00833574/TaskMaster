package com.todochat.todochat.controllers;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.objects.Update;

import com.todochat.todochat.controllers.botcommands.BotCommand;
import com.todochat.todochat.controllers.botcommands.commands.StartCommand;
import com.todochat.todochat.controllers.botcommands.commands.UnknownCommand;


// Clase encargada de manejar los comandos del bot para permitir escalar y agregar nuevos comandos facilmente

public class BotRouter {
    // Instancia 
    private final Map<String, BotCommand> commands = new HashMap<>();
    	private static final Logger logger = LoggerFactory.getLogger(TaskBotController.class);


    public BotRouter(){
        // Aqui definimos los comandos del bot
        commands.put("/start", new StartCommand());
        commands.put("Show Main Screen", new StartCommand());
        commands.put("/addTodo", new StartCommand());
    }

    public void route(Update update, TaskBotController botController) {
        String messageText = update.getMessage().getText();
        String[] messageParts = messageText.split(" ");
        String commandText = messageParts[0];
        String[] arguments = new String[messageParts.length - 1];
        if (messageParts.length > 1) {
            System.arraycopy(messageParts, 1, arguments, 0, messageParts.length - 1);
        }
        BotCommand command = commands.getOrDefault(commandText, new UnknownCommand());
        command.executeCommand(update, botController, arguments);
        
    }
}
