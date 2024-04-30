package com.todochat.todochat.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import com.todochat.todochat.controllers.botcommands.BotCommand;
import com.todochat.todochat.controllers.botcommands.commands.AddTodoCommand;
import com.todochat.todochat.controllers.botcommands.commands.ListTodoCommand;
import com.todochat.todochat.controllers.botcommands.commands.LoginDeveloperCommand;
import com.todochat.todochat.controllers.botcommands.commands.LoginManagerCommand;
import com.todochat.todochat.controllers.botcommands.commands.RegisterManagerCommand;
import com.todochat.todochat.controllers.botcommands.commands.StartCommand;
import com.todochat.todochat.controllers.botcommands.commands.GetDevTasksCommand;
import com.todochat.todochat.controllers.botcommands.commands.GetProyectDevsCommand;
import com.todochat.todochat.controllers.botcommands.commands.ListProjectTasksCommand;
import com.todochat.todochat.controllers.botcommands.commands.UnknownCommand;
import com.todochat.todochat.controllers.botcommands.commands.DeleteTodoCommand;
import com.todochat.todochat.controllers.botcommands.commands.LogoutCommand;
import com.todochat.todochat.controllers.botcommands.commands.MyProjectCommand;
import com.todochat.todochat.controllers.botcommands.commands.UnassignedDevelopersCommand;
import com.todochat.todochat.controllers.botcommands.commands.ViewTodoCommand;
import com.todochat.todochat.controllers.botcommands.commands.changeStatusCommand;
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
    public ListTodoCommand listTodoCommand;
    @Autowired
    public changeStatusCommand changeStatusCommand;
    @Autowired
    public ViewTodoCommand viewTodoCommand;
    @Autowired
    public UnknownCommand unknownCommand;
    @Autowired
    public DeleteTodoCommand deleteTodoCommand;
    @Autowired
    public UnassignedDevelopersCommand unassignedDevelopersCommand;
    public GetDevTasksCommand getDevTasksCommand;

    @Autowired
    public GetProyectDevsCommand getProyectDevsCommand;

    @Autowired
    public AddProyectCommand addProyectCommand;

    @Autowired
    public AddDeveloperCommand addDeveloperCommand;

    @Autowired
    public RemoveDeveloperCommand removeDeveloperCommand;

    @Autowired
    public LoginDeveloperCommand loginDeveloperCommand;
    @Autowired
    public LoginManagerCommand loginManagerCommand;
    @Autowired
    public RegisterManagerCommand registerManagerCommand;
    @Autowired
    public RegisterManagerCommand registerDeveloperCommand;
    @Autowired
    public LogoutCommand LogoutCommand;
    @Autowired
    public MyProjectCommand myProjectCommand;
    @Autowired
    public ListProjectTasksCommand listProjectTasksCommand;

    @PostConstruct
    public void initCommands() {
        commands.put("/start", startCommand);
        commands.put("/login", loginDeveloperCommand);
        commands.put("/getDevTasks", getDevTasksCommand);
        commands.put("/getProjectDevs", getProyectDevsCommand);

        commands.put("/loginDev", loginDeveloperCommand);
        commands.put("/loginManager", loginManagerCommand);
        commands.put("/logout", LogoutCommand);

        commands.put("/registerManager",registerManagerCommand);
        commands.put("/registerDeveloper",registerDeveloperCommand);
        
        commands.put("/deleteTodo", deleteTodoCommand);
        commands.put("/addTask", addTodoCommand);

        commands.put("/unassignedDevs", unassignedDevelopersCommand);
        commands.put("/listTodo", listTodoCommand);
        commands.put("/viewTodo", viewTodoCommand);
        commands.put("/projectTasks", listProjectTasksCommand);
        commands.put("/changeStatus", changeStatusCommand);
    
        commands.put("/addTodo", addTodoCommand);
        commands.put("/addProyect", addProyectCommand);
        commands.put("/myProject", myProjectCommand);
        commands.put("/addDeveloper", addDeveloperCommand);
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
