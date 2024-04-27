package com.todochat.todochat.controllers.botcommands.commands;

import java.util.ArrayList;
import java.util.List;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.todochat.todochat.controllers.TaskBotController;
import com.todochat.todochat.controllers.botcommands.BotCommand;
import com.todochat.todochat.utils.BotLabels;
import com.todochat.todochat.utils.BotMessages;
 

public class StartCommand implements BotCommand {
    @Override
    public void executeCommand(Update update, TaskBotController botController,String[] arguments) {
        long chatId = update.getMessage().getChatId();

        SendMessage messageToTelegram = new SendMessage();
        messageToTelegram.setChatId(chatId);
        messageToTelegram.setText("Selecciona uno de las opciones en el teclado:");

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();

        // first row
        KeyboardRow row = new KeyboardRow();
        row.add(BotLabels.LIST_ALL_ITEMS.getLabel());
        row.add(BotLabels.ADD_NEW_ITEM.getLabel());
        // Add the first row to the keyboard
        keyboard.add(row);

        // second row
        row = new KeyboardRow();
        row.add(BotLabels.SHOW_MAIN_SCREEN.getLabel());
        row.add(BotLabels.HIDE_MAIN_SCREEN.getLabel());
        keyboard.add(row);

        // Set the keyboard
        keyboardMarkup.setKeyboard(keyboard);

        // Add the keyboard markup
        messageToTelegram.setReplyMarkup(keyboardMarkup);

        try {
            botController.execute(messageToTelegram);
        } catch (TelegramApiException e) {
            
        }
    }
}
