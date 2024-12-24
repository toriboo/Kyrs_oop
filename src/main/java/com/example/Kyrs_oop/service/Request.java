package com.example.Kyrs_oop.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class Request {

    public StringBuilder query(String group, String week_day, String week, String season,String year) {
        if (week.isEmpty() || group.isEmpty()||week_day.isEmpty()|| season.isEmpty()|| year.isEmpty()) {
            System.out.println("Ошибка.Пустой запрос");
            System.exit(0);
            return null;
        } else {
            String group_e = URLEncoder.encode(group, StandardCharsets.UTF_8);
            String week_day_e = URLEncoder.encode(week_day, StandardCharsets.UTF_8);
            String week_e = URLEncoder.encode(week, StandardCharsets.UTF_8);
            String season_e = URLEncoder.encode(season, StandardCharsets.UTF_8);
            String year_e =  URLEncoder.encode(year, StandardCharsets.UTF_8);
            String question = "https://digital.etu.ru/api/mobile/schedule?weekDay="+week_day_e+"&subjectType=&groupNumber="+group_e+"&joinWeeks="+week_e+"&season="+season_e+"&year="+year_e+"&withURL=true";
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

