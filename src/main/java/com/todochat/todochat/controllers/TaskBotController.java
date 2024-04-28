package com.todochat.todochat.controllers;

//import java.time.OffsetDateTime;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestBody;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import org.telegram.telegrambots.meta.api.objects.Update;



@Component
public class TaskBotController extends TelegramLongPollingBot {
	private static final Logger logger = LoggerFactory.getLogger(TaskBotController.class);

	private String botName;

	@Autowired
	private BotRouter botRouter;

	public TaskBotController(@Value("${telegram.bot.token}") String botToken,
	@Value("${telegram.bot.name}") String botName) {
		super(botToken);
		logger.info("Bot Token: " + botToken);
		logger.info("Bot name: " + botName);
		this.botName = botName;
		
	}

	@Override
	public void onUpdateReceived(Update update) {
		// Ejecutamos nuestro router
	
		
		if (update.hasMessage() && update.getMessage().hasText()) {
			botRouter.route(update, this);

// 			String messageTextFromTelegram = update.getMessage().getText();
// 			long chatId = update.getMessage().getChatId();

// 			if (messageTextFromTelegram.indexOf(BotLabels.DONE.getLabel()) != -1) {

// 				String done = messageTextFromTelegram.substring(0,
// 						messageTextFromTelegram.indexOf(BotLabels.DASH.getLabel()));
// 				Integer id = Integer.valueOf(done);

// 				try {

// 					Task item = taskService.getTaskById(id);
// 					item.setStatus(Status.COMPLETED);
// 					taskService.updateTask(item);
// 					BotHelper.sendMessageToTelegram(chatId, BotMessages.ITEM_DONE.getMessage(), this);

// 				} catch (Exception e) {
// 					logger.error(e.getLocalizedMessage(), e);
// 				}

// 			} else if (messageTextFromTelegram.indexOf(BotLabels.UNDO.getLabel()) != -1) {

// 				String undo = messageTextFromTelegram.substring(0,
// 						messageTextFromTelegram.indexOf(BotLabels.DASH.getLabel()));
// 				Integer id = Integer.valueOf(undo);

// 				try {
//                     Task item = taskService.getTaskById(id);
// 					item.setStatus(Status.IN_PROGRESS);
// 					taskService.updateTask(item);
// 					BotHelper.sendMessageToTelegram(chatId, BotMessages.ITEM_UNDONE.getMessage(), this);

// 				} catch (Exception e) {
// 					logger.error(e.getLocalizedMessage(), e);
// 				}

// // FALTA STATUS DE PENDING!

// 			} else if (messageTextFromTelegram.indexOf(BotLabels.DELETE.getLabel()) != -1) {

// 				String delete = messageTextFromTelegram.substring(0,
// 						messageTextFromTelegram.indexOf(BotLabels.DASH.getLabel()));
// 				Integer id = Integer.valueOf(delete);

// 				try {
// 					taskService.deleteTaskById(id);
// 					BotHelper.sendMessageToTelegram(chatId, BotMessages.ITEM_DELETED.getMessage(), this);

// 				} catch (Exception e) {
// 					logger.error(e.getLocalizedMessage(), e);
// 				}

// 			} else if (messageTextFromTelegram.equals(BotCommands.HIDE_COMMAND.getCommand())
// 					|| messageTextFromTelegram.equals(BotLabels.HIDE_MAIN_SCREEN.getLabel())) {

// 				BotHelper.sendMessageToTelegram(chatId, BotMessages.BYE.getMessage(), this);

// 			} else if (messageTextFromTelegram.equals(BotCommands.TODO_LIST.getCommand())
// 					|| messageTextFromTelegram.equals(BotLabels.LIST_ALL_ITEMS.getLabel())
// 					|| messageTextFromTelegram.equals(BotLabels.MY_TODO_LIST.getLabel())) {

// 				List<Task> allItems = taskService.getAllTasks();
// 				ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
// 				List<KeyboardRow> keyboard = new ArrayList<>();

// 				// command back to main screen
// 				KeyboardRow mainScreenRowTop = new KeyboardRow();
// 				mainScreenRowTop.add(BotLabels.SHOW_MAIN_SCREEN.getLabel());
// 				keyboard.add(mainScreenRowTop);

// 				KeyboardRow firstRow = new KeyboardRow();
// 				firstRow.add(BotLabels.ADD_NEW_ITEM.getLabel());
// 				keyboard.add(firstRow);

// 				KeyboardRow taskTitleRow = new KeyboardRow();
// 				taskTitleRow.add(BotLabels.MY_TODO_LIST.getLabel());
// 				keyboard.add(taskTitleRow);

// 				List<Task> activeItems = allItems.stream().filter(item -> item.getStatus() == Status.IN_PROGRESS)
// 						.collect(Collectors.toList());

// 				for (Task item : activeItems) {

// 					KeyboardRow currentRow = new KeyboardRow();
// 					currentRow.add(item.getDescription());
// 					currentRow.add(item.getId() + BotLabels.DASH.getLabel() + BotLabels.DONE.getLabel());
// 					keyboard.add(currentRow);
// 				}

// 				List<Task> doneItems = allItems.stream().filter(item -> item.getStatus() == Status.COMPLETED)
// 						.collect(Collectors.toList());

// 				for (Task item : doneItems) {
// 					KeyboardRow currentRow = new KeyboardRow();
// 					currentRow.add(item.getDescription());
// 					currentRow.add(item.getId() + BotLabels.DASH.getLabel() + BotLabels.UNDO.getLabel());
// 					currentRow.add(item.getId() + BotLabels.DASH.getLabel() + BotLabels.DELETE.getLabel());
// 					keyboard.add(currentRow);
// 				}

// 				// command back to main screen
// 				KeyboardRow mainScreenRowBottom = new KeyboardRow();
// 				mainScreenRowBottom.add(BotLabels.SHOW_MAIN_SCREEN.getLabel());
// 				keyboard.add(mainScreenRowBottom);

// 				keyboardMarkup.setKeyboard(keyboard);

// 				SendMessage messageToTelegram = new SendMessage();
// 				messageToTelegram.setChatId(chatId);
// 				messageToTelegram.setText(BotLabels.MY_TODO_LIST.getLabel());
// 				messageToTelegram.setReplyMarkup(keyboardMarkup);

// 				try {
// 					execute(messageToTelegram);
// 				} catch (TelegramApiException e) {
// 					logger.error(e.getLocalizedMessage(), e);
// 				}

// 			} else if (messageTextFromTelegram.equals(BotCommands.ADD_ITEM.getCommand())
// 					|| messageTextFromTelegram.equals(BotLabels.ADD_NEW_ITEM.getLabel())) {
// 				try {
// 					SendMessage messageToTelegram = new SendMessage();
// 					messageToTelegram.setChatId(chatId);
// 					messageToTelegram.setText(BotMessages.TYPE_NEW_TODO_ITEM.getMessage());
// 					// hide keyboard
// 					ReplyKeyboardRemove keyboardMarkup = new ReplyKeyboardRemove(true);
// 					messageToTelegram.setReplyMarkup(keyboardMarkup);

// 					// send message
// 					execute(messageToTelegram);

// 				} catch (Exception e) {
// 					logger.error(e.getLocalizedMessage(), e);
// 				}

// 			}

// 			else {
// 				try {
// 					Task newItem = new Task();
//                     Date currentDate = new Date();
// 					newItem.setDescription(messageTextFromTelegram);
//                     newItem.setFecha_inicio(currentDate);
// 					newItem.setStatus(Status.PENDING);
// 					taskService.createTask(newItem);

// 					SendMessage messageToTelegram = new SendMessage();
// 					messageToTelegram.setChatId(chatId);
// 					messageToTelegram.setText(BotMessages.NEW_ITEM_ADDED.getMessage());

// 					execute(messageToTelegram);
// 				} catch (Exception e) {
// 					logger.error(e.getLocalizedMessage(), e);
// 				}
// 			}
		}
	}

	@Override
	public String getBotUsername() {		
		return botName;
	}

    /*
	// GET /task
	public List<Task> getAllTasks() { 
		return taskService.getAllTasks();
	}

	// GET BY ID /task/{id}
	public ResponseEntity<Task> getTaskById(@PathVariable int id) {
		try {
			ResponseEntity<Task> responseEntity = taskService.getTaskById(id);
			return new ResponseEntity<Task>(responseEntity.getBody(), HttpStatus.OK);
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage(), e);
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	// PUT /task {id}
	public ResponseEntity<Boolean> createTask(@RequestBody Task todoItem) throws Exception {
		Task td = taskService.createTask(task);
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("location", "" + td.getId());
		responseHeaders.set("Access-Control-Expose-Headers", "location");
		// URI location = URI.create(""+td.getID())

		return ResponseEntity.ok().headers(responseHeaders).build();
	}

	// UPDATE /todolist/{id}
	public ResponseEntity<Boolean> updateTask(@RequestBody Task task, @PathVariable int id) {
		try {
			Task task1 = taskService.updateTask(task1, id);
			System.out.println(task1.toString());
			return new ResponseEntity<>(task1, HttpStatus.OK);
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage(), e);
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
	}

	// DELETE todolist/{id}
	public ResponseEntity<Boolean> deleteTaskById(@PathVariable("id") int id) {
		Boolean flag = false;
		try {
			flag = taskService.deleteTaskById(id);
			return new ResponseEntity<>(flag, HttpStatus.OK);
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage(), e);
			return new ResponseEntity<>(flag, HttpStatus.NOT_FOUND);
		}
	}
    */
}
