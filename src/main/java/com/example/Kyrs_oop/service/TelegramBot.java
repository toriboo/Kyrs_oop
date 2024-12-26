package com.example.Kyrs_oop.service;

import com.example.Kyrs_oop.config.BotConfig;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
@AllArgsConstructor


public class TelegramBot extends TelegramLongPollingBot {
    String standart_week_day = "";
    String standart_week = "3";
    String standart_season = "autumn";
    Time time = new Time();
    String standart_year = time.getYear();

    private final BotConfig botConfig;
    private final Request request;
    private final ResponseParser responseParser;

    @Autowired
    public TelegramBot(BotConfig botConfig, Request request, ResponseParser responseParser) {
        this.botConfig = botConfig;
        this.request = request;
        this.responseParser = responseParser;
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {

        String responseText = "";

        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            if (messageText.equals("/start")) {
                startCommand(chatId, update.getMessage().getChat().getFirstName());
            } else if (messageText.equals("/help")) {
                helpCommand(chatId);

            } else if (messageText.startsWith("/show_all_schedule")) {
                show_all_command(chatId, messageText);
            } else if(messageText.startsWith("/show_tomorrow_schedule")){
                show_tomorrow_command(chatId, messageText);
            }else if (messageText.equals("/choose_semestr")){
                semestrKeyboard(chatId);
            }
            else if (messageText.equals("/choose_week")){
                weekKeyboard(chatId);
            }
            else {
                try {
                    defaultCommand(chatId);
                } catch (ParseException e) {
                    throw new RuntimeException("Unable to parse date");
                }
                sendMessage(chatId, responseText);
            }

        }
        else if(update.hasCallbackQuery()){
            String call_data = update.getCallbackQuery().getData();
            Long chatId = update.getCallbackQuery().getMessage().getChatId();
            String answer;
            if (call_data.equals("autumn")) {
                standart_season = "autumn";
                answer = "Семестр по умолчанию: " + standart_season;
                sendMessage(chatId,answer);
            }
            else if (call_data.equals("spring")){
                standart_season = "spring";
                answer = "Семестр по умолчанию: " + standart_season;
                sendMessage(chatId,answer);
            }
            else if (call_data.equals("1")){
                standart_week = "1";
                answer = "Неделя по умолчанию: " + standart_week;
                sendMessage(chatId,answer);
            }
            else if (call_data.equals("2")){
                standart_week = "2";
                answer = "Неделя по умолчанию: " + standart_week;
                sendMessage(chatId,answer);
            }
            else if (call_data.equals("3")){
                standart_week = "3";
                answer = "Неделя по умолчанию: две недели";
                sendMessage(chatId,answer);
            }
        }
    }

    private void startCommand(Long chatId, String name) {
        String answer = "Привет, " + name + ",приятно познакомиться!" + "\n" +
                "Я могу подсказать расписание занятий" + "\n" +
                "Для описания комманд отправь  /help в чат" ;
        sendMessage(chatId, answer);
    }
    private void helpCommand(Long chatId){
        String answer = "Вот список команд: " + "\n" +
                "/start - перезапуск бота\n"  +
                "/help - список комманд\n" +
                "/show_all_schedule {Группа} - показывает все расписание группы\n" +
                "/show_tomorrow_schedule {Группа} {Номер недели} - показывает расписание на завтра (Если номер недели не указан,то покажется расписние недели по умолчанию\n"
                + "/choose_semestr - выбрать семестр по умолчанию )\n" +
                "/choose_week - выбрать неделю по умолчанию\n" ;
        sendMessage(chatId, answer);
    }
    private void defaultCommand(Long chatId){
        String answer = "К сожалению, данной команды нет. Попробуйте заново";
        sendMessage(chatId, answer);
    }

    private void show_all_command(Long chatId, String messageText) {
        String[] part_of_message = messageText.split(" ");
        String answer = null;
        if (part_of_message.length != 2) {
            answer = "Неверная команда. Попробуйте ввести /show_all_schedule + {Номер группы}";
        }
        else {
            String number_group = part_of_message[1];
            if (number_group.length() != 4) {
                answer = "Номер группы должен содержать 4 цифры";
            }
            else {
                String response = String.valueOf(request.query(number_group, standart_week_day, standart_season, standart_year));
                answer = responseParser.parsing(response, number_group, standart_week);
            }

        }
        sendMessage(chatId, answer);
    }

    private void show_tomorrow_command(Long chatId, String messageText){
        String[] part_of_message = messageText.split(" ");
        String answer = null;
        if (part_of_message.length == 2) {
            String number_group = part_of_message[1];
            if (number_group.length() != 4) {
                answer = "Номер группы должен содержать 4 цифры";
            } else {
                String weekDay = String.valueOf(time.getNextDayOfWeek().subSequence(0,3));
                String response = String.valueOf(request.query(number_group, weekDay, standart_season, standart_year));
                answer = responseParser.parsing(response, number_group,standart_week);
            }
        } else if (part_of_message.length == 3) {
            String number_group = part_of_message[1];
            String week_number = part_of_message[2];
            if (number_group.length() != 4) {
                answer = "Номер группы должен содержать 4 цифры";
            }
            else if(week_number.equals("1")||week_number.equals("2")){

                String weekDay = String.valueOf(time.getNextDayOfWeek().subSequence(0,3));
                String response = String.valueOf(request.query(number_group, weekDay, standart_season, standart_year));
                answer = responseParser.parsing(response, number_group, week_number);
            }

            else {
                answer = "Неделя может быть только нечетной (1) или четной (2)";
            }

        } else {
            answer = "Неверная команда. Попробуйте ввести /show_tomorrow_schedule + {Номер группы} \n или /show_tomorrow_schedule + {Номер группы} + {номер недели} ";
        }
        sendMessage(chatId, answer);
    }




    private void sendMessage(Long chatId, String textToSend){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(textToSend);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {

        }
    }
    private void semestrKeyboard(Long chatId) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton autumnButton = new InlineKeyboardButton();
        autumnButton.setText("осень");
        autumnButton.setCallbackData("autumn");

        InlineKeyboardButton springButton = new InlineKeyboardButton();
        springButton.setText("весна");
        springButton.setCallbackData("spring");
        List<InlineKeyboardButton> keyboardButtons = new ArrayList<>();
        keyboardButtons.add(autumnButton);
        keyboardButtons.add(springButton);
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        rows.add(keyboardButtons);
        inlineKeyboardMarkup.setKeyboard(rows);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText("Выберите семестр по умолчанию:"); // текст сообщения
        sendMessage.setReplyMarkup(inlineKeyboardMarkup); // установка клавиатуры

        try {
            execute(sendMessage); // отправка сообщения
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    private void weekKeyboard(Long chatId) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton week2 = new InlineKeyboardButton();
        week2.setText("четная");
        week2.setCallbackData("2");

        InlineKeyboardButton week1 = new InlineKeyboardButton();
        week1.setText("нечетная");
        week1.setCallbackData("1");
        InlineKeyboardButton week3 = new InlineKeyboardButton();
        week3.setText("две недели");
        week3.setCallbackData("3");
        List<InlineKeyboardButton> keyboardButtons = new ArrayList<>();
        keyboardButtons.add(week1);
        keyboardButtons.add(week2);
        keyboardButtons.add(week3);
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        rows.add(keyboardButtons);
        inlineKeyboardMarkup.setKeyboard(rows);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText("Выберите неделю по умолчанию:");
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}





