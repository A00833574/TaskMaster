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
import com.todochat.todochat.controllers.botcommands.commands.GetProjectDevsCommand;
import com.todochat.todochat.controllers.botcommands.commands.ListProjectTasksCommand;
import com.todochat.todochat.controllers.botcommands.commands.UnknownCommand;
import com.todochat.todochat.controllers.botcommands.commands.DeleteTodoCommand;
import com.todochat.todochat.controllers.botcommands.commands.LogoutCommand;
import com.todochat.todochat.controllers.botcommands.commands.MyProjectCommand;
import com.todochat.todochat.controllers.botcommands.commands.RegisterDeveloperCommand;
import com.todochat.todochat.controllers.botcommands.commands.UnassignedDevelopersCommand;
import com.todochat.todochat.controllers.botcommands.commands.ViewTodoCommand;
import com.todochat.todochat.controllers.botcommands.commands.changeStatusCommand;
import com.todochat.todochat.controllers.botcommands.commands.AddProjectCommand;
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
    @Autowired
    public GetDevTasksCommand getDevTasksCommand;
    @Autowired
    public GetProjectDevsCommand getProjectDevsCommand;
    @Autowired
    public AddProjectCommand addProjectCommand;
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
    public RegisterDeveloperCommand registerDeveloperCommand;
    @Autowired
    public LogoutCommand LogoutCommand;
    @Autowired
    public MyProjectCommand myProjectCommand;
    @Autowired
    public ListProjectTasksCommand listProjectTasksCommand;

    @PostConstruct
    public void initCommands() {
        // Navegacion
        commands.put("/start", startCommand); // Testeado manager y enviado

        // Auth
        commands.put("/loginDev", loginDeveloperCommand); // Testeado y mandado
        commands.put("/loginManager", loginManagerCommand); // Testeado y mandado
        commands.put("/logout", LogoutCommand); // Testeado y mandado

        // Register
        commands.put("/registerManager",registerManagerCommand); // Testeado y mandado
        commands.put("/registerDeveloper",registerDeveloperCommand); // Testeado y mandado

        // Developers
        commands.put("/deleteTodo", deleteTodoCommand); // Testeado y mandado
        commands.put("/addTask", addTodoCommand); // Testeado y mandado
        commands.put("/listTodo", listTodoCommand); // Testeado y mandado
        commands.put("/changeStatus", changeStatusCommand); // Testeado y mandado
        
        // Managers
        commands.put("/getDevTasks", getDevTasksCommand); // Testeado y mandado
        commands.put("/getProjectDevs", getProjectDevsCommand); // Testeado y mandado
        commands.put("/unassignedDevs", unassignedDevelopersCommand); // Testeado y mandado
        commands.put("/projectTasks", listProjectTasksCommand); // Testeado y mandado
        commands.put("/addProject", addProjectCommand); 
        commands.put("/myProject", myProjectCommand); // Testeado y mandado
        commands.put("/addDeveloper", addDeveloperCommand); // Testeado y mandado
        commands.put("/removeDeveloper", removeDeveloperCommand); // Testeado y mandado
        
        // Developers-Managers
        commands.put("/viewTodo", viewTodoCommand); // Testeado y mandado
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
