package com.example.Kyrs_oop.service;

import com.example.Kyrs_oop.config.BotConfig;

import lombok.AllArgsConstructor;
import org.springframework.expression.ParseException;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.springframework.stereotype.Component;


@Component
@AllArgsConstructor


public class TelegramBot extends TelegramLongPollingBot {
    String standart_week_day = "";
    String standart_week = "3";
    String standart_season = "autumn";
    String standart_year = "2024";

    private final BotConfig botConfig;

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
            } else {
                try {
                    defaultCommand(chatId);
                } catch (ParseException e) {
                    throw new RuntimeException("Unable to parse date");
                }
                sendMessage(chatId, responseText);
            }

        }

    }



    private void startCommand(Long chatId, String name) {
        String answer = "Привет, " + name + ",приятно познакомится!" + "\n" +
                "Я могу подсказать расписание занятий" + "\n" +
                "Для описания комманд отправь  /help в чат" ;
        sendMessage(chatId, answer);
    }
    private void helpCommand(Long chatId){
        String answer = "Вот список команд: " + "\n" +
                "/start - перезапуск бота\n"  +
                "/help - список комманд\n" +
                "/show_all_schedule {Группа} - показывает все расписание группы";
        sendMessage(chatId, answer);
    }
    private void defaultCommand(Long chatId){
        String answer = "К солжалению, данной команды нет. Попробуйте заново";
        sendMessage(chatId, answer);
    }

    private void show_all_command(Long chatId, String messageText){
        String[] part_of_message  = messageText.split(" ");
        String answer;
        if (part_of_message.length != 2){
            answer = "Неверная команда. Попробуйте ввести /show_all_schedule + {Номер группы}";

        }
        else{
            String number_group = part_of_message[1];
            if ( number_group.length() != 4){
                answer = "Номер группы должен содержать 4 цифры";
            }
            else{
                Request request = new Request();
                String response = String.valueOf(request.query(number_group, standart_week_day, standart_week, standart_season,standart_year));
                ResponseParser responseParser = new ResponseParser();
                answer = responseParser.parsing(response, number_group, 7);
            }
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

}





