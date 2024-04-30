package com.todochat.todochat.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import com.todochat.todochat.controllers.botcommands.BotCommand;
import com.todochat.todochat.controllers.botcommands.commands.AddTodoCommand;
import com.todochat.todochat.controllers.botcommands.commands.LoginDeveloperCommand;
import com.todochat.todochat.controllers.botcommands.commands.StartCommand;
import com.todochat.todochat.controllers.botcommands.commands.UnknownCommand;
import com.todochat.todochat.controllers.botcommands.commands.AddProyectCommand;
import com.todochat.todochat.controllers.botcommands.commands.AddDeveloperCommand;
import com.todochat.todochat.controllers.botcommands.commands.RemoveDeveloperCommand;

import jakarta.annotation.PostConstruct;

// Clase encargada de manejar los comandos del bot para permitir escalar y agregar nuevos comandos facilmente

@Component
public class BotRouter {
    // Instancia
    private final Map<String, BotCommand> commands = new HashMap<>();

    @Autowired
    public StartCommand startCommand;

    @Autowired
    public AddTodoCommand addTodoCommand;

    @Autowired
    public UnknownCommand unknownCommand;

    @Autowired
    public AddProyectCommand addProyectCommand;

    @Autowired
    public AddDeveloperCommand addDeveloperCommand;

    @Autowired
    public RemoveDeveloperCommand removeDeveloperCommand;

    @Autowired
    public LoginDeveloperCommand loginDeveloperCommand;

    @PostConstruct
    public void initCommands() {
        commands.put("/start", startCommand);
        commands.put("Show Main Screen", startCommand);
        commands.put("/login", loginDeveloperCommand);
    
        commands.put("/addTodo", addTodoCommand);
        commands.put("/addProyect", addProyectCommand);
        commands.put("/addDeveloper", addProyectCommand);
        commands.put("/removeDeveloper", removeDeveloperCommand);
    }

    public void route(Update update, TaskBotController botController) {
        String messageText = update.getMessage().getText();

        // Eliminar cualquier texto que esté entre paréntesis al inicio del mensaje
        messageText = messageText.replaceFirst("^\\(.*?\\) ?", "");

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
        BotCommand command = commands.getOrDefault(commandText, unknownCommand);

        // Ejecutar el comando con los argumentos obtenidos
        command.executeCommand(update, botController, arguments);

    }
}
