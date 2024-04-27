package com.todochat.todochat.controllers.botcommands;

import org.telegram.telegrambots.meta.api.objects.Update;

import com.todochat.todochat.controllers.TaskBotController;

// Clase abstracta
public interface BotCommand {
    void executeCommand (Update update, TaskBotController botController,String[] arguments);
}
