package com.example.Kyrs_oop.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Iterator;


public class ResponseParser {

    public String parsing(String response, String group ,int count_day) {
        ObjectMapper objectMapper = new ObjectMapper();
        String teacher = null;
        StringBuilder answer;
        try {
            JsonNode rootNode = objectMapper.readTree(response);
            JsonNode groupNode = rootNode.get(group);
            for (int i = 0; i < count_day; i ++){

                JsonNode daysNode = groupNode.get("days").get(String.valueOf(i));
                String dayName = daysNode.get("name").asText();
                System.out.println("Day Name: " + dayName);



                JsonNode lessonsNode = daysNode.get("lessons");
                for (JsonNode lesson : lessonsNode) {
                    teacher = lesson.get("teacher").asText();
                    String lessonName = lesson.get("name").asText();
                    System.out.println("Lesson: " + lessonName + ", Teacher: " + teacher);
                }

            }
            return printJsonNode(groupNode, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Ошибка";

    }
    private static String printJsonNode(JsonNode node, int level) {
        StringBuilder answer = new StringBuilder();
        if (node.isObject()) {
            Iterator<String> fieldNames = node.fieldNames();
            while (fieldNames.hasNext()) {
                String fieldName = fieldNames.next();
                JsonNode childNode = node.get(fieldName);

                answer.append("fieldName \n");
                printJsonNode(childNode, level + 1);
            }
        }
        else if (node.isArray()) {
            for (int i = 0; i < node.size(); i++) {

                printJsonNode(node.get(i), level + 1);
            }
        }
        else {

            answer.append(node.asText());
        }
        return answer.toString();
    }



}


