package com.todochat.todochat.controllers;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.objects.Update;

import com.todochat.todochat.controllers.botcommands.BotCommand;
import com.todochat.todochat.controllers.botcommands.commands.AddTodoCommand;
import com.todochat.todochat.controllers.botcommands.commands.StartCommand;
import com.todochat.todochat.controllers.botcommands.commands.UnknownCommand;
import com.todochat.todochat.services.TaskService;

// Clase encargada de manejar los comandos del bot para permitir escalar y agregar nuevos comandos facilmente

public class BotRouter {
    // Instancia
    private final Map<String, BotCommand> commands = new HashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(TaskBotController.class);

    public BotRouter(TaskService taskService) {
        // Aqui definimos los comandos del bot
        commands.put("/start", new StartCommand());
        commands.put("Show Main Screen", new StartCommand());
        commands.put("/addTodo", new AddTodoCommand(taskService));
    }

    public void route(Update update, TaskBotController botController) {
        String messageText = update.getMessage().getText();

        // Separar el mensaje completo en partes basadas en "-"
        String[] messageParts = messageText.split("-");

        // Extraer el comando, eliminando el prefijo "/"
        String commandText = messageParts[0];

        // Preparar el arreglo de argumentos con el tamaño adecuado
        String[] arguments = new String[messageParts.length - 1];

        // Copiar los argumentos del mensaje al arreglo
        for (int i = 1; i < messageParts.length; i++) {
            arguments[i - 1] = messageParts[i];
        }

        // Obtener el comando correspondiente, o un comando desconocido si no existe
        BotCommand command = commands.getOrDefault(commandText, new UnknownCommand());

        // Ejecutar el comando con los argumentos obtenidos
        command.executeCommand(update, botController, arguments);

    }
}
