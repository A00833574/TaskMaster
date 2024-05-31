package com.todochat.todochat.services;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import com.todochat.todochat.controllers.TaskBotController;

public class TelegramService {
    @SuppressWarnings("unused")
    private Update update;
    private TaskBotController botController;
    private ReplyKeyboardMarkup keyboardMarkup;
    private SendMessage messageToTelegram;

    private static final Logger logger = LoggerFactory.getLogger(TaskBotController.class);

    // Constructor que inicializa la clase con un objeto Update y el controlador del bot
    public TelegramService(Update update, TaskBotController botController) {
        this.update = update;
        this.botController = botController;
        this.messageToTelegram = new SendMessage();
        this.messageToTelegram.setChatId(update.getMessage().getChatId());

        this.keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        this.keyboardMarkup.setKeyboard(keyboard);
    }

    // Getter for keyboardMarkup
    public ReplyKeyboardMarkup getKeyboardMarkup() {
        return keyboardMarkup;
    }

    // Metodo para agregar a una tabla
    public void addRow(String text) {
        KeyboardRow row = new KeyboardRow();
        row.add(text);
        this.keyboardMarkup.getKeyboard().add(row);
    }

    public void addRow(List<String> texts) {
        KeyboardRow row = new KeyboardRow();
        row.addAll(texts);
        this.keyboardMarkup.getKeyboard().add(row);
    }

    // Metodo para limpiar la tabla
    public void clearRow() {
        if (this.keyboardMarkup != null) {
            this.keyboardMarkup.setKeyboard(new ArrayList<>());
        } else {
            this.keyboardMarkup = new ReplyKeyboardMarkup();
            this.keyboardMarkup.setKeyboard(new ArrayList<>());
        }
    }

    // Método para enviar un mensaje
    public void sendMessage(String messageText) {
        try {
            this.messageToTelegram.setText(messageText);
            if (this.keyboardMarkup.getKeyboard() != null) {
                this.messageToTelegram.setReplyMarkup(this.keyboardMarkup);
            }
            botController.execute(messageToTelegram);
        } catch (Exception e) {
            logger.error("Error al mandar mensaje " + e.getMessage(), e);
        }
    }
}
