package com.example.Kyrs_oop.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class Request {

    public StringBuilder query(String user_request) {
        if (user_request.isEmpty()) {
            System.out.println("Ошибка.Пустой запрос");
            System.exit(0);
            return null;
        } else {
            String user_request_encode = URLEncoder.encode(user_request, StandardCharsets.UTF_8);
            String question = "https://digital.etu.ru/api/mobile/schedule?weekDay=MON&subjectType=%D0%9B%D0%B5%D0%BA&groupNumber=3351&joinWeeks=true&season=autumn&year=2024&withURL=true";
            return sendGetRequest(question);
        }

    }

    StringBuilder sendGetRequest(String question) {
        try {
            // запрос
            URL url = new URL(question);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // чтение ответа
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            in.close();
            connection.disconnect();
            return response;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

