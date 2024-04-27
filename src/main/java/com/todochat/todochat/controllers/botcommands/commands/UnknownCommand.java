package com.todochat.todochat.controllers.botcommands.commands;

import org.telegram.telegrambots.meta.api.objects.Update;

import com.todochat.todochat.controllers.TaskBotController;
import com.todochat.todochat.controllers.botcommands.BotCommand;

public class UnknownCommand implements BotCommand{
    @Override
    public void executeCommand(Update update, TaskBotController botController, String[] arguments){
        // Notificar que el comando no es valido
    }
}
