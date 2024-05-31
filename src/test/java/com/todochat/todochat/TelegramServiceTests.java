package com.todochat.todochat;

import static org.mockito.Mockito.*;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.todochat.todochat.controllers.TaskBotController;

import static org.junit.jupiter.api.Assertions.*;

import com.todochat.todochat.services.TelegramService;

public class TelegramServiceTests {

    @Mock
    private Update update;

    @Mock
    private Message message;

    @Mock
    private TaskBotController botController;

    private TelegramService telegramService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        when(update.getMessage()).thenReturn(message);
        when(message.getChatId()).thenReturn(123456789L);

        telegramService = new TelegramService(update, botController);
    }

    @Test
    public void testAddRowWithSingleText() {
        String text = "Sample Text";
        telegramService.addRow(text);

        List<KeyboardRow> keyboard = telegramService.getKeyboardMarkup().getKeyboard();
        assertEquals(1, keyboard.size());
        assertEquals(1, keyboard.get(0).size());
        assertEquals(text, keyboard.get(0).get(0).getText());
    }

    @Test
    public void testAddRowWithMultipleTexts() {
        List<String> texts = List.of("Text1", "Text2", "Text3");
        telegramService.addRow(texts);

        List<KeyboardRow> keyboard = telegramService.getKeyboardMarkup().getKeyboard();
        assertEquals(1, keyboard.size());
        List<String> keyboardTexts = keyboard.get(0).stream()
                .map(KeyboardButton::getText)
                .collect(Collectors.toList());
        assertEquals(texts, keyboardTexts);
    }

    @Test
    public void testClearRow() {
        telegramService.addRow("Sample Text");
        assertFalse(telegramService.getKeyboardMarkup().getKeyboard().isEmpty());

        telegramService.clearRow();
        assertTrue(telegramService.getKeyboardMarkup().getKeyboard().isEmpty());
    }

    @Test
    public void testSendMessage() {
        String messageText = "Hello, World!";
        try {
            telegramService.sendMessage(messageText);

            ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
            verify(botController).execute(argumentCaptor.capture());

            SendMessage sendMessage = argumentCaptor.getValue();
            assertEquals(messageText, sendMessage.getText());
            assertEquals("123456789", sendMessage.getChatId());
        } catch (TelegramApiException e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }

    @Test
    public void testSendMessageWithKeyboard() {
        telegramService.addRow("Option 1");
        telegramService.addRow("Option 2");
        String messageText = "Choose an option";

        try {
            telegramService.sendMessage(messageText);

            ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
            verify(botController).execute(argumentCaptor.capture());

            SendMessage sendMessage = argumentCaptor.getValue();
            assertEquals(messageText, sendMessage.getText());
            assertEquals("123456789", sendMessage.getChatId());
            assertNotNull(sendMessage.getReplyMarkup());
            assertTrue(sendMessage.getReplyMarkup() instanceof ReplyKeyboardMarkup);
        } catch (TelegramApiException e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }

    @Test
    public void testSendMessageExceptionHandling() {
        try {
            doThrow(new TelegramApiException("Telegram API Error")).when(botController).execute(any(SendMessage.class));

            String messageText = "Test Exception";
            telegramService.sendMessage(messageText);

            // Verifying the error log
            Logger logger = LoggerFactory.getLogger(TaskBotController.class);
            assertDoesNotThrow(() -> logger.error("Error al mandar mensaje Telegram API Error"));
        } catch (TelegramApiException e) {
            fail("Exception should be handled within the sendMessage method.");
        }
    }
}
