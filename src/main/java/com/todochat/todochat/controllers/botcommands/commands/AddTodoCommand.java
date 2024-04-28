package com.todochat.todochat.controllers.botcommands.commands;

import java.util.Date;


import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;


import com.todochat.todochat.controllers.TaskBotController;
import com.todochat.todochat.controllers.botcommands.BotCommand;
import com.todochat.todochat.models.Task;
import com.todochat.todochat.models.enums.Status;

import com.todochat.todochat.services.TaskService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

 
@Component
public class AddTodoCommand implements BotCommand {
    
    @Autowired
    private TaskService taskService;

	private static final Logger logger = LoggerFactory.getLogger(TaskBotController.class);

    

    @Override
    public void executeCommand(Update update, TaskBotController botController,String[] arguments) {
        long chatId = update.getMessage().getChatId();
        

        try {
            Task newItem = new Task();
            Date currentDate = new Date();
            newItem.setName(arguments[0]);
            newItem.setDescription(arguments[1]);
            newItem.setFecha_inicio(currentDate);
            newItem.setStatus(Status.PENDING);
            taskService.createTask(newItem);

            SendMessage messageToTelegram = new SendMessage();
            messageToTelegram.setChatId(chatId);
            messageToTelegram.setText("Tarea agregada correctamente");
          

            botController.execute(messageToTelegram);
        } catch (Exception e) {
            logger.error("Error al agregar una tarea", e);
        }
    }
}
