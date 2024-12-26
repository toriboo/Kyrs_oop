package com.example.Kyrs_oop.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;


@Component
public class ResponseParser {

    public String parsing(String response, String group, String week_number) {
        ObjectMapper objectMapper = new ObjectMapper();
        String teacher;
        String start_time ;
        String room ;
        String week;
        String subjectType;
        StringBuilder answer = new StringBuilder();
        try {
            JsonNode rootNode = objectMapper.readTree(response);
            JsonNode groupNode = rootNode.get(group);
            for (int i = 0; i < 7; i ++){

                JsonNode daysNode = groupNode.get("days").get(String.valueOf(i));
                if (daysNode != null) {
                    String dayName = daysNode.get("name").asText();
                    answer.append('\n' + dayName + '\n' + '\n');

                    JsonNode lessonsNode = daysNode.get("lessons");
                    for (JsonNode lesson : lessonsNode) {
                        teacher = lesson.get("teacher").asText();
                        start_time = lesson.get("start_time").asText();
                        room = lesson.get("room").asText();
                        week = lesson.get("week").asText();
                        subjectType = lesson.get("subjectType").asText();
                        String lessonName = lesson.get("name").asText();
                        if (week_number.equals("2") || week_number.equals("1")) {
                            if(week.equals(week_number)) {
                                answer.append("Неделя: " + week + '\n');
                                answer.append(start_time + ": " + lessonName + " " + subjectType + '\n');
                                answer.append("Преподаватель: " + teacher + " ауд. " + room + '\n');
                            }
                        }

                        else {
                            answer.append("Неделя: " + week + '\n');
                            answer.append(start_time + ": " + lessonName + " " + subjectType + '\n');
                            answer.append("Преподаватель: " + teacher + " ауд. " + room + '\n');
                        }

                    }
                }

            }
            return answer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Ошибка запроса";

    }

}


