package com.example.Kyrs_oop.bot;

import com.example.Kyrs_oop.config.BotConfig;
import com.example.Kyrs_oop.service.ApiResponse;
import com.example.Kyrs_oop.service.Request;
import com.example.Kyrs_oop.service.ResponseParser;
import lombok.AllArgsConstructor;
import com.example.Kyrs_oop.module.CurrencyModel;
import org.springframework.expression.ParseException;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@AllArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {
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

        if(update.hasMessage() && update.getMessage().hasText()){
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            switch (messageText){
                case "/start":
                    startCommand(chatId, update.getMessage().getChat().getFirstName());
                    break;
                case "/help":
                    helpCommand(chatId);
                    break;
                case "/test":
                    testCommand(chatId);
                    break;
                default:
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
                "/test...";
        sendMessage(chatId, answer);
    }
    private void defaultCommand(Long chatId){
        String answer = "К солжалению, данной команды нет. Попробуйте заново";
        sendMessage(chatId, answer);
    }
    private void testCommand(Long chatId){
        Request request = new Request();
        String response = String.valueOf(request.query("3351"));
        ResponseParser responseParser = new ResponseParser();
        Map<String, ApiResponse.GroupData> answer = responseParser.parsing(response);
        sendMessage(chatId, answer.toString());
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




